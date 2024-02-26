package com.example.minhalistadecomprasapp

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Itens(

    val id: Int,
    var name: String,
    var quantidade: Int,
    var isEditing: Boolean = false

)

@Composable
fun ShoppingListApp()
{
    var sItens by remember { mutableStateOf(listOf<Itens>()) }
    var showDialog by remember { mutableStateOf(false) }
    var NomeItem by remember { mutableStateOf("") }
    var QuantidadeItem by remember { mutableStateOf("") }


    Column(

        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    )
    {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)

        )
        {

            
            Text(text = "Adicionar Item");
        }

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        {
            items(sItens)
            {
                item ->
                if(item.isEditing)
                {
                    ShoppingItemEditor(item = item, onEditComplete = {

                        editedName, editedQuantity ->
                        sItens = sItens.map { it.copy(isEditing = false) }
                        val editedItem = sItens.find { it.id == item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantidade = editedQuantity
                        }
                    })
                } else
                {
                    ShoppingListItem(item = item, onEditClick = {

                        sItens = sItens.map { it.copy(isEditing = it.id == item.id) }

                    }, onDeleteClick = {

                        sItens = sItens-item

                    })
                }
            }
        }
    }


    if (showDialog)
    {
        AlertDialog(onDismissRequest = {showDialog = false},
            confirmButton = {

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){

                             Button(

                                 onClick = {

                                     if(NomeItem.isNotBlank())
                                     {
                                        val novoItem = Itens(
                                            id = sItens.size + 1,
                                            name = NomeItem,
                                            quantidade = QuantidadeItem.toInt(),


                                        )

                                         sItens = sItens + novoItem;
                                         showDialog = false
                                         NomeItem = "";
                                         QuantidadeItem = "1";
                                     }

                             }) {
                                 Text(text = "Adicionar")
                             } 
                             
                             Button(onClick = { showDialog = false }) {
                                 Text(text = "Cancelar")
                             }   


                            }
            },
            title = { Text(text = "Adicionar Item")},
            text = {
                Column {
                    OutlinedTextField(value = NomeItem,
                        onValueChange = {NomeItem = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                        )

                    OutlinedTextField(value = QuantidadeItem,
                        onValueChange = {QuantidadeItem = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
            )
    }

}

@Composable
fun ShoppingItemEditor(item: Itens, onEditComplete: (String, Int) -> Unit)
{
    var editName by remember { mutableStateOf(item.name) }
    var editQuantity by remember { mutableStateOf(item.quantidade.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {

        Column {

            BasicTextField(
                value = editName,
                onValueChange = {editName = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

            BasicTextField(
                value = editQuantity,
                onValueChange = {editQuantity = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

        }

        Button(

            onClick = {

                isEditing = false
                onEditComplete(editName, editQuantity.toIntOrNull() ?: 1)

            }

        ){

            Text(text = "Salvar");
            
        }


    }


}


@Composable
fun ShoppingListItem(

    item: Itens,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit


)
{

    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.DarkGray),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ){

        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = " Qntd: ${item.quantidade}", modifier = Modifier.padding(8.dp))

        Row (modifier = Modifier.padding(8.dp)){
            
            
            IconButton(onClick = onEditClick ) {

                Icon(imageVector = Icons.Default.Edit , contentDescription = null)
                
            }


            IconButton(onClick = onDeleteClick) {

                Icon(imageVector = Icons.Default.Delete , contentDescription = null)

            }
            

        }

        
    }


}
