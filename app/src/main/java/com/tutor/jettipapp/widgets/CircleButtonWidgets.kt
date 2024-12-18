package com.tutor.jettipapp.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

var IconSizeModifier = Modifier;
@Composable
fun CircleButtonWidgets(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.Black.copy(alpha = 0.7f),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    elevation: Dp = 8.dp
) {
    Card(
        modifier = modifier
            .padding(all = 4.dp)
            .clickable { onClick.invoke() }.then(IconSizeModifier),
        shape = CircleShape,
        colors = CardDefaults.cardColors(backgroundColor),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Icon(imageVector = imageVector, contentDescription = "Plus or Minus", tint = tint)
    }
}