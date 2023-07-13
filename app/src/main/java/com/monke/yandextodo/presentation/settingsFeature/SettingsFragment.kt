package com.monke.yandextodo.presentation.settingsFeature

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.presentation.theme.AppTheme
import com.monke.yandextodo.presentationState.settingsFeature.SettingsViewModel
import com.monke.yandextodo.presentationState.settingsFeature.SettingsViewModelFactory
import javax.inject.Inject


class SettingsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: SettingsViewModelFactory
    private val viewModel: SettingsViewModel by viewModels {
        viewModelFactory
    }

    override fun onAttach(context: Context) {
        (activity?.applicationContext as App).applicationComponent
            .mainTodoActivityComponent().inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme() {
                    SettingsScreen()
                }

            }
        }
    }


    @Composable
    private fun SettingsScreen() {
        Scaffold(
            topBar = { TopAppBar() }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            ) {
                ThemesMenu()
//                TextField(
//                    value = "lalala",
//                    onValueChange = {},
//                    readOnly = true)
            }
        }
    }

    @Composable
    private fun TopAppBar() {
        return AppTheme() {
            TopAppBar(
                elevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(
                        start = 8.dp,
                    ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        getString(R.string.settings),
                        style = MaterialTheme.typography.h2,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ThemesMenu() {
        val themesArray = resources.getStringArray(R.array.themes_array)
        val themesModes = arrayOf(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES
        )
        var expanded by remember { mutableStateOf(false) }
        val settings = viewModel.appSettings.collectAsState()

        val themesMap: Map<Int, String> = themesModes.zip(themesArray).toMap()

        return ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            Box() {
                TextField(
                    value = themesMap.getOrDefault(
                        settings.value.themeMode,
                        themesArray[0]),
                    label = { Text(getString(R.string.app_theme)) },
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = MaterialTheme.colors.onPrimary
                    )
                )
            }


            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                themesModes.forEach { item ->
                    DropdownMenuItem(
                        onClick = {
                            viewModel.setAppThemeMode(item)
                            expanded = false
                        }
                    ) {
                        Text(themesMap[item]!!)
                    }
                }
            }
        }
    }



    @Preview
    @Composable
    private fun SettingsScreenPreview() {
        return AppTheme {
            Scaffold(
                topBar = { TopAppBarPreview() },
            ) { padding ->
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("Hello world")
                }
            }
        }

    }



    @Preview
    @Composable
    private fun TopAppBarPreview() {
        return TopAppBar(
            elevation = 0.dp
        ) {
            Row(
                modifier = Modifier.padding(
                    start = 8.dp,
                ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
                Text(
                    "Settings",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }


}