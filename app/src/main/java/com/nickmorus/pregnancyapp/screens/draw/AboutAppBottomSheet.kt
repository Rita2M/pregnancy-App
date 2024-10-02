package com.nickmorus.pregnancyapp.screens.draw

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nickmorus.pregnancyapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutApp(
    onDismiss: () -> Unit,
    onUserAgreementClicked:()-> Unit,
    onSourceInformationClicked:()->Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )



    ModalBottomSheet(onDismissRequest = { onDismiss() },
        sheetState = sheetState)
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()



        ) {
            Image(
                painter = painterResource(id = R.drawable.log),
                contentDescription = "Description of the image"
            )
            Text(
                text = stringResource(id = R.string.about_app_text),


            )

            TextButton(onClick = { onUserAgreementClicked()}
            ) {
                Text(text = stringResource(id = R.string.user_agreement),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start )
            }
                TextButton(onClick = { onSourceInformationClicked()}, modifier = Modifier.padding(bottom = 40.dp)  ) {
                Text(text = stringResource(id = R.string.sources_information))

            }


        }

    }
}

@Preview(showBackground = true)
@Composable
fun AboutAppPreview() {
    AboutApp(
        onDismiss = { },
        onUserAgreementClicked = {},
        onSourceInformationClicked = {}

    )
}
