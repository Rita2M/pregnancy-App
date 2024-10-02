package com.nickmorus.pregnancyapp.screens.main.more

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nickmorus.pregnancyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoBottomSheet(
    onDismiss: () -> Unit,

) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )



    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()


        ) {
            Image(
                painter = painterResource(id = R.drawable.log),
                contentDescription = "Description of the image"
            )
            Text(
                text = stringResource(id = R.string.about_app_text),


                )
        }
    }
}
