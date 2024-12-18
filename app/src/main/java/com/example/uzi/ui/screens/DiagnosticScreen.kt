package com.example.uzi.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.uzi.R
import com.example.uzi.data.models.SectorPoint
import com.example.uzi.data.repository.MockUziServiceRepository
import com.example.uzi.ui.components.canvas.ZoomableCanvasSectorWithConstraints
import com.example.uzi.ui.components.containers.FormationInfoContainer
import com.example.uzi.ui.theme.Paddings
import com.example.uzi.ui.viewModel.diagnosticHistory.DiagnosticHistoryViewModel
import org.beyka.tiffbitmapfactory.TiffBitmapFactory


@SuppressLint("NewApi")
@Composable
fun DiagnosticScreen(
    diagnosticDate: String,
    clinicName: String,
    diagnosticHistoryViewModel: DiagnosticHistoryViewModel,
) {
//    val nestedScrollConnection = remember {
//        object : NestedScrollConnection {
//            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
//                // Приоритет отдаётся зумируемому компоненту
//                return if (/* зумируемый компонент активен */) {
//                    Offset.Zero // Блокируем колонку
//                } else {
//                    super.onPreScroll(available, source)
//                }
//            }
//        }
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
//            .padding(horizontal = Paddings.Medium)
    ) {
        var isFullScreenOpen by remember { mutableStateOf(false) }
        val uiState = diagnosticHistoryViewModel.uiState.collectAsState().value

        println(uiState.currentResponse.uzi?.dateOfAdmission)
        println(uiState.currentResponse.uzi?.clinicName)
        println(uiState.currentResponse.uzi)
        TextButton(
            onClick = {
//                onAndroidBackClick(
//                    navController = navController,
//                    viewModel = newDiagnosticViewModel
//                )
            },
//            enabled = newDiagnosticUiState.currentScreenIndex > 0
        ) {
            Text(
                text = "Назад",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Text(
            text = "Диагностика от ${uiState.currentResponse.uzi?.dateOfAdmission}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
        Text(
            text = uiState.currentResponse.uzi?.clinicName ?: "Нет названия клиники",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = Color.Black
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Paddings.Medium)
        ) {
            var selectedTabIndex by remember { mutableStateOf(0) }
            val tabs = listOf("Обработанный снимок", "Исходный снимок")

            TabRow(
                selectedTabIndex = selectedTabIndex,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) } // Текст вкладки
                    )
                }
            }

            val points = listOf(
                SectorPoint(100, 100),
                SectorPoint(200, 100),
                SectorPoint(200, 200),
                SectorPoint(100, 300)
            )

            val context = LocalContext.current
            val imageUri: Uri? = uiState.currentResponse.images?.get(0)?.url

            println("imageUri: $imageUri")
            println("lol")

// Переменная для отслеживания текущей страницы
            val currentPage = remember { mutableStateOf(0) }
            val numberOfDirectories = remember { mutableStateOf(1f) }
            val mimeType = remember { mutableStateOf("") }
            val bitmap: Bitmap? = imageUri?.let { uri ->
                mimeType.value = context.contentResolver.getType(uri) ?: ""
                println("mimeType: $mimeType")
                if (mimeType.value == "image/tiff") {
                    try {
                        // Сначала читаем метаинформацию
                        val initialDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
                        initialDescriptor?.use { descriptor ->
                            val options = TiffBitmapFactory.Options()

                            options.inJustDecodeBounds = true
                            TiffBitmapFactory.decodeFileDescriptor(descriptor.fd, options)
                            numberOfDirectories.value = options.outDirectoryCount.toFloat()
                        }

                        // Затем декодируем изображение текущей страницы
                        val pageDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
                        pageDescriptor?.use { descriptor ->
                            val options = TiffBitmapFactory.Options()
                            options.inDirectoryNumber = currentPage.value
                            options.inJustDecodeBounds = false
                            TiffBitmapFactory.decodeFileDescriptor(descriptor.fd, options)
                        }
                    } catch (e: Exception) {
                        println(e)
                        null
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        val source = ImageDecoder.createSource(context.contentResolver, uri)
                        ImageDecoder.decodeBitmap(source)
                    } else {
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    }
                }
            }


            if (mimeType.value == "image/tiff") {
                Column(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                ) {
                    var sliderPosition by remember { mutableFloatStateOf(0f) }

                    Slider(
                        value = sliderPosition,
                        onValueChange = {
                            sliderPosition = it
                            currentPage.value = it.toInt()
                        },
                        valueRange = 0f..numberOfDirectories.value - 1
                    )

                    // Отображаем текущую страницу
                    Text(
                        text = "Страница: ${currentPage.value + 1}",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }


            ZoomableCanvasSectorWithConstraints(
                imageBitmap = bitmap?.asImageBitmap() ?: ImageBitmap.imageResource(R.drawable.paint),
                pointsList = if (selectedTabIndex == 1) emptyList() else uiState.currentResponse.segments?.get(currentPage.value)?.contor ?: points,
                onFullScreen = { isFullScreenOpen = true },
            )

//          TODO(Здесь надо пользоваться viewModel с репозиторием)
            uiState.currentResponse.formations?.forEachIndexed { i, formation ->
                FormationInfoContainer(
                    formationIndex = i,
                    formationClass = formation.formationClass,
                    formationProbability = formation.maxTirads,
                    formationDescription = "Описание формации"
                )
            }
        }

        if (isFullScreenOpen) {
            Dialog(onDismissRequest = { isFullScreenOpen = false }) {
                UziImageFullScreen(
                    imageBitmap = ImageBitmap.imageResource(R.drawable.paint),
                    pointsList = listOf(
                        SectorPoint(100, 100),
                        SectorPoint(200, 100),
                        SectorPoint(200, 200),
                        SectorPoint(100, 300)
                    )
                )
            }
        }



    }
}


@Preview
@Composable
fun DiagnosticScreenPreview() {
    DiagnosticScreen(
        diagnosticDate = "24.11.2024",
        clinicName = "Клиника",
        diagnosticHistoryViewModel = DiagnosticHistoryViewModel(
            MockUziServiceRepository()
        )
    )
}