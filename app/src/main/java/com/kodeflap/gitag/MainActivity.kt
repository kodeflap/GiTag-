package com.kodeflap.gitag

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.kodeflap.gitag.ui.theme.GiTagTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private val imageUriState = mutableStateOf<Uri?>(null)
    private val metadataState = mutableStateOf<String?>(null)

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUriState.value = it
            metadataState.value = GitTag.extractMetadata(this, it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GiTagTheme {
                MainScreen(
                    imageUri = imageUriState.value,
                    metadataJson = metadataState.value,
                    onPickImage = { pickImage.launch("image/*") },
                    onRemoveMetadata = { uri ->
                        uri?.let {
                            val updatedUri = GitTag.removeMetadata(this, it)
                            imageUriState.value = updatedUri
                            metadataState.value = GitTag.extractMetadata(this, updatedUri)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MainScreen(
    imageUri: Uri?,
    metadataJson: String?,
    onPickImage: () -> Unit,
    onRemoveMetadata: (Uri) -> Unit
) {
    // Use rememberScrollState to handle scrolling
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState), // Add vertical scrolling capability
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onPickImage) {
            Text(text = "Select Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )

            metadataJson?.let { json ->
                DisplayMetadata(json)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onRemoveMetadata(uri) }) {
                    Text(text = "Remove Metadata")
                }
            }
        }
    }
}

@Composable
fun DisplayMetadata(metadataJson: String) {
    val currentMetadataJson by rememberUpdatedState(metadataJson)
    val metadata = parseJson(currentMetadataJson)

    Column(modifier = Modifier.fillMaxWidth()) {
        metadata.forEach { (label, value) ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "$label:", style = MaterialTheme.typography.bodyMedium)
                Text(text = value ?: "N/A", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

fun parseJson(json: String): Map<String, String?> {
    val map = mutableMapOf<String, String?>()
    val jsonObject = JSONObject(json)

    jsonObject.keys().forEach { key ->
        map[key] = jsonObject.optString(key, "N/A")
    }

    return map
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    GiTagTheme {
        MainScreen(
            imageUri = null,
            metadataJson = null,
            onPickImage = {},
            onRemoveMetadata = {}
        )
    }
}
