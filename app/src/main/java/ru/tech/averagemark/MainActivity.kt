package ru.tech.averagemark

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import ru.tech.averagemark.ui.theme.AverageMarkTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (viewModel.needUpdate) {
            viewModel.updateText(
                getSharedPreferences(
                    "sharedPrefs",
                    Context.MODE_PRIVATE
                ).getString("AVERAGE", "") ?: ""
            )
        }

        setContent {
            AverageMarkTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.weight(0.3f))
                        Text(
                            "Ваша оценка",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 30.sp,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(Modifier.size(10.dp))
                        Text(
                            viewModel.averageState.value.ifEmpty { "n/a" },
                            color = viewModel.color.value,
                            textAlign = TextAlign.Center,
                            fontSize = 50.sp,
                            fontStyle = FontStyle.Italic
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            viewModel.text.value,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 20.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.size(20.dp))
                        Row {
                            SmallFloatingActionButton(onClick = {
                                viewModel.removeLast()
                            }) {
                                Icon(Icons.Rounded.Remove, null)
                            }
                            SmallFloatingActionButton(onClick = {
                                viewModel.removeAll()
                            }) {
                                Icon(Icons.Outlined.Delete, null)
                            }
                        }
                        Spacer(Modifier.size(20.dp))
                        Row {
                            listOf("2", "3", "4", "5").forEach {
                                FloatingActionButton(onClick = {
                                    viewModel.updateText(it)
                                }, modifier = Modifier.padding(horizontal = 4.dp)) {
                                    Text(it, fontSize = 16.sp)
                                }
                            }
                        }
                        Spacer(Modifier.weight(0.3f))
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.save(
            getSharedPreferences(
                "sharedPrefs",
                Context.MODE_PRIVATE
            )
        )
    }
}