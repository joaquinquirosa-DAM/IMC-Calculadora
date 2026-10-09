package org.insbaixcamp.imccalculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.insbaixcamp.imccalculadora.ui.theme.HackerBg
import org.insbaixcamp.imccalculadora.ui.theme.HackerBorder
import org.insbaixcamp.imccalculadora.ui.theme.HackerCyan
import org.insbaixcamp.imccalculadora.ui.theme.HackerGray
import org.insbaixcamp.imccalculadora.ui.theme.HackerGreen
import org.insbaixcamp.imccalculadora.ui.theme.HackerGreenBright
import org.insbaixcamp.imccalculadora.ui.theme.HackerGreenDark
import org.insbaixcamp.imccalculadora.ui.theme.HackerGreenDim
import org.insbaixcamp.imccalculadora.ui.theme.HackerRed
import org.insbaixcamp.imccalculadora.ui.theme.HackerSurface
import org.insbaixcamp.imccalculadora.ui.theme.HackerTextSecondary
import org.insbaixcamp.imccalculadora.ui.theme.HackerYellow
import org.insbaixcamp.imccalculadora.ui.theme.ImcCalculadoraTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImcCalculadoraTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = HackerBg
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        HackerScanlineBackground()
                        BMIScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun HackerScanlineBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val strokeWidth = 1.dp.toPx()
        val spacing = 8.dp.toPx()
        var y = 0f
        val lineAlpha = 0.05f

        while (y < size.height) {
            drawLine(
                color = HackerGreen,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth,
                alpha = lineAlpha
            )
            y += spacing
        }
    }
}

@Composable
fun BMIScreen() {
    var nom by remember { mutableStateOf("") }
    var pes by remember { mutableIntStateOf(80) }
    var alçada by remember { mutableIntStateOf(180) }
    var imc by remember { mutableDoubleStateOf(0.0) }
    var isCalculated by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Terminal Header Box
        TerminalHeader()

        // Name Input Box
        CyberTextField(
            value = nom,
            onValueChange = { nom = it },
            label = "> SUBJECT_IDENTIFIER",
            placeholder = "ENTER_TARGET_NAME"
        )

        // Weight Input Box
        CyberWeightCard(
            weight = pes,
            onWeightChange = { newWeight ->
                pes = newWeight.coerceIn(20, 250)
            }
        )

        // Height Input Box
        CyberHeightCard(
            height = alçada,
            onHeightChange = { newHeight ->
                alçada = newHeight.coerceIn(100, 230)
            }
        )

        // Calculation Trigger Button
        CyberButton(
            text = "[ ▶ EXECUTE_BMI_DIAGNOSTIC ]",
            onClick = {
                val heightInMeters = alçada.toDouble() / 100.0
                imc = pes.toDouble() / (heightInMeters * heightInMeters)
                isCalculated = true
            }
        )

        // Diagnostic Output Section
        AnimatedVisibility(
            visible = isCalculated,
            enter = fadeIn(animationSpec = tween(400)) + slideInVertically(animationSpec = tween(400))
        ) {
            DiagnosticResultCard(
                name = nom,
                imc = imc,
                height = alçada
            )
        }

        // Terminal Footer Status
        SystemLogFooter()
    }
}

@Composable
fun TerminalHeader() {
    val infiniteTransition = rememberInfiniteTransition(label = "cursor")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursor_alpha"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, HackerBorder, CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp)),
        colors = CardDefaults.cardColors(containerColor = HackerSurface),
        shape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "SYSTEM://IMC_ANALYZER_v3.0.4",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = HackerGreenBright
                )
                Text(
                    text = "_",
                    style = MaterialTheme.typography.titleMedium,
                    color = HackerGreenBright,
                    modifier = Modifier.alpha(cursorAlpha)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "[STATUS: ONLINE]  [MODE: UNRESTRICTED]  [SEC: 256-BIT]",
                style = MaterialTheme.typography.labelMedium,
                color = HackerTextSecondary
            )
        }
    }
}

@Composable
fun CyberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, HackerGreenDark, RoundedCornerShape(4.dp)),
        colors = CardDefaults.cardColors(containerColor = HackerSurface),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = HackerGreen
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = HackerGreenDim,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = HackerGreenBright,
                    fontWeight = FontWeight.Bold
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HackerBorder,
                    unfocusedBorderColor = HackerGray,
                    focusedContainerColor = HackerBg,
                    unfocusedContainerColor = HackerBg,
                    cursorColor = HackerGreenBright
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }
    }
}

@Composable
fun CyberWeightCard(
    weight: Int,
    onWeightChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, HackerGreenDark, RoundedCornerShape(4.dp)),
        colors = CardDefaults.cardColors(containerColor = HackerSurface),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "> DATA_01 // BODY_MASS",
                    style = MaterialTheme.typography.labelLarge,
                    color = HackerGreen
                )
                Text(
                    text = "UNIT: KG",
                    style = MaterialTheme.typography.labelMedium,
                    color = HackerTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Readout
                Box(
                    modifier = Modifier
                        .background(HackerBg, RoundedCornerShape(4.dp))
                        .border(1.dp, HackerGreenDim, RoundedCornerShape(4.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = String.format(Locale.getDefault(), "%03d KG", weight),
                        style = MaterialTheme.typography.displayMedium,
                        color = HackerGreenBright,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Cyber Action Buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CyberMiniButton(text = "-5") { onWeightChange(weight - 5) }
                    CyberMiniButton(text = "-1") { onWeightChange(weight - 1) }
                    CyberMiniButton(text = "+1") { onWeightChange(weight + 1) }
                    CyberMiniButton(text = "+5") { onWeightChange(weight + 5) }
                }
            }
        }
    }
}

@Composable
fun CyberMiniButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CutCornerShape(4.dp))
            .background(HackerGray)
            .border(1.dp, HackerBorder, CutCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = HackerGreenBright,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CyberHeightCard(
    height: Int,
    onHeightChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, HackerGreenDark, RoundedCornerShape(4.dp)),
        colors = CardDefaults.cardColors(containerColor = HackerSurface),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "> DATA_02 // VERTICAL_HEIGHT",
                    style = MaterialTheme.typography.labelLarge,
                    color = HackerGreen
                )
                Text(
                    text = "$height CM",
                    style = MaterialTheme.typography.titleMedium,
                    color = HackerGreenBright,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(
                value = height.toFloat(),
                onValueChange = { onHeightChange(it.toInt()) },
                valueRange = 100f..230f,
                colors = SliderDefaults.colors(
                    thumbColor = HackerGreenBright,
                    activeTrackColor = HackerGreen,
                    inactiveTrackColor = HackerGreenDim
                )
            )
        }
    }
}

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .border(1.5.dp, HackerBorder, CutCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = HackerGreenDark,
            contentColor = HackerGreenBright
        ),
        shape = CutCornerShape(8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun DiagnosticResultCard(
    name: String,
    imc: Double,
    height: Int
) {
    val (category, statusColor, statusBadge) = when {
        imc < 18.5 -> Triple("UNDERWEIGHT", HackerCyan, "[ STATUS: CRITICAL_LOW ]")
        imc < 25.0 -> Triple("NORMAL WEIGHT", HackerGreenBright, "[ STATUS: OPTIMAL ]")
        imc < 30.0 -> Triple("OVERWEIGHT", HackerYellow, "[ STATUS: ELEVATED_MASS ]")
        else -> Triple("OBESE", HackerRed, "[ STATUS: HIGH_RISK_MASS ]")
    }

    val minHealthyWeight = 18.5 * (height / 100.0) * (height / 100.0)
    val maxHealthyWeight = 24.9 * (height / 100.0) * (height / 100.0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, statusColor, CutCornerShape(topEnd = 12.dp, bottomStart = 12.dp)),
        colors = CardDefaults.cardColors(containerColor = HackerSurface),
        shape = CutCornerShape(topEnd = 12.dp, bottomStart = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = ">>> DIAGNOSTIC_REPORT",
                        style = MaterialTheme.typography.titleMedium,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = statusBadge,
                        style = MaterialTheme.typography.labelMedium,
                        color = statusColor,
                        fontSize = 11.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(statusColor.copy(alpha = 0.5f))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SUBJECT: ${if (name.isBlank()) "UNKNOWN_TARGET" else name.uppercase()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HackerGreen
                )
                Text(
                    text = "CAT: $category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
            }

            // Big BMI Readout Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HackerBg, RoundedCornerShape(4.dp))
                    .border(1.dp, statusColor.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "BMI INDEX",
                        style = MaterialTheme.typography.labelMedium,
                        color = HackerTextSecondary
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "%.2f", imc),
                        style = MaterialTheme.typography.displayLarge,
                        color = statusColor,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Visual Scale Meter Bar
            Text(
                text = "BMI SPECTRUM ANALYSIS",
                style = MaterialTheme.typography.labelMedium,
                color = HackerTextSecondary
            )
            BmiSpectrumMeter(currentImc = imc)

            // Healthy Weight Recommendation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HackerGreenDark.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = String.format(
                        Locale.getDefault(),
                        "RECOMMENDED MASS FOR %dCM: %.1f KG - %.1f KG",
                        height, minHealthyWeight, maxHealthyWeight
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = HackerGreenBright,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun BmiSpectrumMeter(currentImc: Double) {
    val fraction = ((currentImc - 15.0) / (40.0 - 15.0)).coerceIn(0.0, 1.0).toFloat()

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(HackerBg)
                .border(1.dp, HackerGreenDim, RoundedCornerShape(3.dp))
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(3.5f)
                        .fillMaxSize()
                        .background(HackerCyan.copy(alpha = 0.6f))
                )
                Box(
                    modifier = Modifier
                        .weight(6.4f)
                        .fillMaxSize()
                        .background(HackerGreenBright.copy(alpha = 0.6f))
                )
                Box(
                    modifier = Modifier
                        .weight(5.0f)
                        .fillMaxSize()
                        .background(HackerYellow.copy(alpha = 0.6f))
                )
                Box(
                    modifier = Modifier
                        .weight(10.1f)
                        .fillMaxSize()
                        .background(HackerRed.copy(alpha = 0.6f))
                )
            }

            // Indicator Needle
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val xPos = size.width * fraction
                    drawLine(
                        color = Color.White,
                        start = Offset(xPos, 0f),
                        end = Offset(xPos, size.height),
                        strokeWidth = 3.dp.toPx()
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "15.0", style = MaterialTheme.typography.labelMedium, color = HackerTextSecondary, fontSize = 10.sp)
            Text(text = "18.5", style = MaterialTheme.typography.labelMedium, color = HackerCyan, fontSize = 10.sp)
            Text(text = "25.0", style = MaterialTheme.typography.labelMedium, color = HackerGreenBright, fontSize = 10.sp)
            Text(text = "30.0", style = MaterialTheme.typography.labelMedium, color = HackerYellow, fontSize = 10.sp)
            Text(text = "40.0", style = MaterialTheme.typography.labelMedium, color = HackerRed, fontSize = 10.sp)
        }
    }
}

@Composable
fun SystemLogFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "[0x7F00] SYSTEM_READY // IMC_CORE_ACTIVE // MEM_OK",
            style = MaterialTheme.typography.labelMedium,
            color = HackerGreenDim,
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050B05)
@Composable
fun GreetingPreview() {
    ImcCalculadoraTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(HackerBg)
        ) {
            HackerScanlineBackground()
            BMIScreen()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050B05)
@Composable
fun ResultCardPreview() {
    ImcCalculadoraTheme {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(HackerBg)
        ) {
            DiagnosticResultCard(
                name = "NEO",
                imc = 24.69,
                height = 180
            )
        }
    }
}
