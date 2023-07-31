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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.monke.yandextodo.App
import com.monke.yandextodo.R
import com.monke.yandextodo.domain.AppSettings
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
                    SettingsScreen(
                        viewModel.appSettings.collectAsState()
                    )
                }

            }
        }
    }

    @Composable
    @Preview
    private fun SettingsScreenLightPreview() {
        val settings = remember {
            mutableStateOf(AppSettings(themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)) }
        return AppTheme(darkTheme = false) {
            SettingsScreen(settings)
        }
    }

    @Composable
    @Preview
    private fun SettingsScreenDarkPreview() {
        val settings = remember {
            mutableStateOf(AppSettings(themeMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)) }
        return AppTheme(darkTheme = true) {
            SettingsScreen(settings)
        }
    }


    @Composable
    private fun SettingsScreen(
        appSettings: State<AppSettings>
    ) {
        Scaffold(
            topBar = { TopAppBar() }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp)
            ) {
                ThemesMenu(appSettings)
            }
        }
    }

    @Composable
    private fun TopAppBar() {
        return TopAppBar(
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Row(
                modifier = Modifier.padding(
                    start = 8.dp,
                ),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = {
                        findNavController().popBackStack()
                    }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.onPrimary
                    )
                }

                Text(
                    stringResource(R.string.settings),
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun ThemesMenu(
        settings: State<AppSettings>
    ) {
        val themesArray = stringArrayResource(R.array.themes_array)
        val themesModes = arrayOf(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES
        )
        var expanded by remember { mutableStateOf(false) }

        val themesMap: Map<Int, String> = themesModes.zip(themesArray).toMap()

        return ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = it
            }
        ) {
            TextField(
                value = themesMap.getOrDefault(
                    settings.value.themeMode,
                    themesArray[0]),
                label = { Text(stringResource(R.string.app_theme)) },
                onValueChange = {},
                readOnly = true,
                enabled = false,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = MaterialTheme.colors.onPrimary
                ),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
                modifier = Modifier.fillMaxWidth()
            )

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


}