package com.example.alarm

import android.os.Bundle
import android.util.Range
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.alarm.alarmManager.AlarmItem
import com.example.alarm.alarmManager.AlarmScheudlerImp
import com.example.alarm.ui.theme.AlarmTheme
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlarmTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

//                    maxkeppelerDatePicker()
                    vanpraPickedDateDialog()


                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun maxkeppelerDatePicker() {
    val state = rememberSheetState()
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }


    CalendarDialog(
        state = state,
        config = CalendarConfig(
            style = CalendarStyle.MONTH,
        ),
        selection = CalendarSelection.Date(
            selectedDate = selectedDate.value
        ) { newDate ->
            selectedDate.value = newDate
        },
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            state.show()
        }) {
            Text(text = "Pick date")
        }
        if (selectedDate.value != null) {
            Text(text = selectedDate.value.toString())
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
//            timeDialogState.show()
        }) {
            Text(text = "Pick time")
        }
//        Text(text = formattedTime)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun vanpraPickedDateDialog(

) {
    val context = LocalContext.current
        //for Alarm Manager
    val scheduler = AlarmScheudlerImp(context)
    var alarmItem: AlarmItem? = null
    // State for get time from Picker
    var pickedDate by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedTime by remember {
        mutableStateOf(LocalTime.NOON)
    }
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("MMM dd yyyy").format(pickedDate)
        }
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("hh:mm").format(pickedTime)
        }
    }
    //vanpra dialog for time and date
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            dateDialogState.show()
        }) {
            Text(text = "Pick date")
        }
        Text(text = formattedDate)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            timeDialogState.show()
        }) {
            Text(text = "Pick time")
        }
        Text(text = formattedTime)
        Spacer(modifier = Modifier.height(80.dp))
        OutlinedButton(modifier = Modifier.align(Alignment.CenterHorizontally),onClick = {
            alarmItem = AlarmItem(
                time = LocalDateTime.of(pickedDate,pickedTime),
                messege = ""
            )
            alarmItem?.let(scheduler::schedule)
//            secondsText = ""
//            message = ""

        }) {
            Text("set Alarm")
        }
    }



    MaterialDialog(
//        border = BorderStroke(10.dp, Color.Gray),
//        backgroundColor = Color.Green,
        properties = DialogProperties(
            dismissOnBackPress = true

        ),
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
                Toast.makeText(
                    context, "Clicked ok", Toast.LENGTH_LONG
                ).show()
            }
            negativeButton(text = "Cancel")
        }) {
        datepicker(initialDate = LocalDate.now(), title = "Pick a date", allowedDateValidator = {
//            it.isAfter(LocalDate.now())
            it.isEqual(LocalDate.now())
        }) {
            pickedDate = it
        }
    }
    MaterialDialog(dialogState = timeDialogState, buttons = {
        positiveButton(text = "Ok") {
            Toast.makeText(
                context, "Clicked ok", Toast.LENGTH_LONG
            ).show()
        }
        negativeButton(text = "Cancel")
    }) {
        timepicker(
            initialTime = LocalTime.NOON,
            title = "Pick a time",
            timeRange = LocalTime.MIDNIGHT..LocalTime.NOON
        ) {
            pickedTime = it
        }
    }


    @Composable
    fun MainScreen(
        scheduler: AlarmScheudlerImp, alarmItem: AlarmItem?
    ) {
        Scaffold(topBar = {
            TopAppBar(colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text("Top app bar")
            })
        }, bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Bottom app bar",
                )
            }
        }, floatingActionButton = {

        }) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                vanpraPickedDateDialog()


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AlarmTheme {}
}