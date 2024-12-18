package com.tutor.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tutor.jettipapp.components.InputField
import com.tutor.jettipapp.ui.theme.JetTipAppTheme
import com.tutor.jettipapp.util.calculatePerPeople
import com.tutor.jettipapp.util.calculateTip
import com.tutor.jettipapp.widgets.CircleButtonWidgets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
//                TopHeader()
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun TopHeader(totalAmount: Double = 254.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 20.dp)
            .height(175.dp)
            .clip(shape = RoundedCornerShape(12.dp)),
        color = Color(0xCBC28AF8)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* TODO:  Format Value */
            val value = "%.2f".format(totalAmount)
            /* END OF TODO */
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$$value",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview
@Composable
fun MainContent() {
    val splitState = remember {
        mutableStateOf(1)
    }

    val totalPerPerson = remember {
        mutableStateOf(0.0)
    }

    val totalTipAmount = remember {
        mutableStateOf(0.0)
    }

    Column {
        BillForm(
            totalTipAmount = totalTipAmount,
            splitState = splitState,
            totalPerPerson = totalPerPerson
        )
    }

}

@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    onValChange: (String) -> Unit = {},
    totalTipAmount: MutableState<Double>,
    splitState: MutableState<Int>,
    totalPerPerson: MutableState<Double>
) {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val sliderOpt = remember {
        mutableStateOf(0f)
    }

    val sliderValue = (sliderOpt.value * 100).toInt()

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopHeader(totalAmount = totalPerPerson.value)
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    // TODO - OnValueChange
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                })

//            if (validState) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Split Text")
                Row(
                    modifier = Modifier.padding(all = 4.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    CircleButtonWidgets(
                        imageVector = Icons.Default.Remove,
                        onClick = {
                            if (splitState.value <= 1) return@CircleButtonWidgets
                            splitState.value--

                            totalPerPerson.value = calculatePerPeople(
                                totalBillState.value.toDouble(),
                                splitState.value,
                                sliderValue
                            )
                        })
                    Text(
                        text = splitState.value.toString().trim(),
                        modifier = modifier
                            .padding(horizontal = 10.dp)
                            .align(Alignment.CenterVertically)
                    )
                    CircleButtonWidgets(
                        imageVector = Icons.Default.Add,
                        onClick = {
                            splitState.value++

                            totalPerPerson.value = calculatePerPeople(
                                totalBillState.value.toDouble(),
                                splitState.value,
                                sliderValue
                            )
                        })
                }
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Tip")
                Text(text = "$${totalTipAmount.value}", modifier = modifier.padding(end = 20.dp))
            }
            Text(text = "${sliderValue}%")
            Spacer(modifier = modifier.height(16.dp))
            Slider(
                modifier = modifier.padding(horizontal = 25.dp),
                value = sliderOpt.value,
                enabled = totalBillState.value.toString().isNotEmpty(),
                onValueChange = { newValue ->
                    sliderOpt.value = newValue

                    // Calculate Tip
                    totalTipAmount.value =
                        calculateTip(totalBillState.value.toDouble(), sliderValue)

                    // Calculate Total Per Person
                    totalPerPerson.value = calculatePerPeople(
                        totalBillState.value.toDouble(),
                        splitState.value,
                        sliderValue
                    )

                })
//            } else {
//                Box() {}
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TipAppPreview() {
    MyApp {
        Text(text = "Hello")
    }
}