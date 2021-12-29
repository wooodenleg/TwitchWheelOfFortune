import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ItemEditor(
    items: List<String>,
    onItemsChanged: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {


    Column(modifier) {
        items.forEachIndexed { index, item ->
            OutlinedTextField(
                value = item,
                onValueChange = { newItemName ->
                    onItemsChanged(items.mapIndexed { referenceIndex, name ->
                        if (referenceIndex == index) newItemName else name
                    })
                },
                trailingIcon = {
                    IconButton({ onItemsChanged(items - item) }) {
                        Icon(Icons.Default.Delete, null)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(SpacingSmall))
        }

        IconButton(
            onClick = { onItemsChanged(items + "") },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Icon(Icons.Default.Add, null)
        }
    }
}