package com.example.myapplication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroViewModel : ViewModel() {
    private val _documento = MutableStateFlow("")
    val documento: StateFlow<String> = _documento.asStateFlow()

    private val _fecha = MutableStateFlow("")
    val fecha: StateFlow<String> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow("")
    val hora: StateFlow<String> = _hora.asStateFlow()

    fun onDocumentoChange(nuevoDocumento: String) {
        _documento.value = nuevoDocumento
    }

    fun onFechaChange(nuevaFecha: String) {
        _fecha.value = nuevaFecha
    }

    fun onHoraChange(nuevaHora: String) {
        _hora.value = nuevaHora
    }
}

@Composable
fun PantallaRegistro(
    modifier: Modifier = Modifier,
    viewModel: RegistroViewModel = viewModel()
) {
    val documento by viewModel.documento.collectAsState()
    val fecha by viewModel.fecha.collectAsState()
    val hora by viewModel.hora.collectAsState()

    var mostrarDatePicker by remember { mutableStateOf(false) }
    var mostrarTimePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = documento,
            onValueChange = { viewModel.onDocumentoChange(it) },
            label = { Text("Documento") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Box {
            OutlinedTextField(
                value = fecha,
                onValueChange = { viewModel.onFechaChange(it) },
                label = { Text("Fecha") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable { mostrarDatePicker = true }
            )
        }

        Box {
            OutlinedTextField(
                value = hora,
                onValueChange = { viewModel.onHoraChange(it) },
                label = { Text("Hora") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable { mostrarTimePicker = true }
            )
        }
    }

    if (mostrarDatePicker) {
        SelectorDeFecha(
            fechaSeleccionada = { viewModel.onFechaChange(it) },
            alCerrar = { mostrarDatePicker = false }
        )
    }

    if (mostrarTimePicker) {
        SelectorDeHora(
            horaSeleccionada = { viewModel.onHoraChange(it) },
            alCerrar = { mostrarTimePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorDeFecha(
    fechaSeleccionada: (String) -> Unit,
    alCerrar: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = alCerrar,
        confirmButton = {
            TextButton(
                onClick = {
                    state.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val dateString = formatter.format(Date(millis))
                        fechaSeleccionada(dateString)
                    }
                    alCerrar()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorDeHora(
    horaSeleccionada: (String) -> Unit,
    alCerrar: () -> Unit
) {
    val state = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = alCerrar,
        confirmButton = {
            TextButton(
                onClick = {
                    val hora = state.hour
                    val minuto = state.minute
                    val horaString = String.format(Locale.getDefault(), "%02d:%02d", hora, minuto)
                    horaSeleccionada(horaString)
                    alCerrar()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = alCerrar) {
                Text("Cancelar")
            }
        },
        text = {
            TimePicker(state = state)
        }
    )
}
