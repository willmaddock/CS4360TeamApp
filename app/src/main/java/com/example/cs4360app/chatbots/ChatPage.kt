package com.example.cs4360app.chatbots

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs4360app.R
import com.example.cs4360app.activities.MapsActivity
import com.example.cs4360app.models.ChatViewModel
import com.example.cs4360app.models.MessageModel
import kotlinx.coroutines.flow.asFlow

@Preview(showBackground = true)
@Composable
fun SimpleComposablePreview() {
    ChatPage(viewModel = ChatViewModel(LocalContext.current))
}

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel)
{
    val context = LocalContext.current // Get the current context

    // Load the chat history from shared preferences
    val sharedPreferences = context.getSharedPreferences("chat_history", Context.MODE_PRIVATE)
    val chatHistory = sharedPreferences.getStringSet("chat_history", mutableSetOf()) ?: mutableSetOf()
    viewModel.messageList.addAll(chatHistory.map {
        val splitMessage = it.split(":", limit = 3)
        MessageModel(splitMessage[1], splitMessage[0], splitMessage[2])
    })

    Column(modifier = modifier) {
        AppHeader()
        MessageList(modifier = Modifier.weight(1f),messageList = viewModel.messageList)
        MessageInput(onMessageSend = {
            viewModel.sendMessage(it)
        })

        // Add a back button
        CustomButton(onClick = {
            // Create an intent to start MapsActivity
            val intent = Intent(context, MapsActivity::class.java)
            context.startActivity(intent)
        })
    }
}

@Composable
fun CustomButton(onClick: () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color.Cyan, // Change this to your desired button color
            onPrimary = Color.Black // Change this to your desired content (text/icon) color
        )
    ) {
        androidx.compose.material3.Button(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(5.dp).fillMaxWidth())
        {
            Text("Back to Maps")
        }
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier,messageList: List<MessageModel>) {
    if (messageList.isEmpty()){
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Icon(
                modifier = Modifier.size(60.dp),
                painter = painterResource(id = R.drawable.question_and_answer_icon),
                contentDescription = "Chat Icon",
                tint = Color.Cyan,
            )
            Text(text = "Ask me anything", fontSize = 24.sp)
        }
    } else{
        LazyColumn(
            modifier = modifier,
            reverseLayout = true
        ) {
            items(messageList.reversed()){
                MessageRow(messageModel = it)
            }
        }
    }
}


@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "Model"
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier.fillMaxWidth()
            ){
                Box(
                    modifier = Modifier.align(
                        if(isModel) Alignment.BottomStart else Alignment.BottomEnd
                    ).padding(
                        start = if(isModel) 8.dp else 70.dp,
                        end = if(isModel) 70.dp else 8.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                        .clip(RoundedCornerShape(50f))
                        .background(if(isModel) Color.Magenta else  Color.Cyan)
                        .padding(16.dp)
                ){
                    SelectionContainer {
                        Text(
                            text = messageModel.message,
                            fontWeight = FontWeight.W500
                        )
                    }
                }
            }
        }
        // Display the timestamp
        Text(
            text = messageModel.timestamp,
            fontSize = 12.sp,
            modifier = Modifier.align(if(isModel) Alignment.Start else Alignment.End)
        )
    }
}

@Composable
fun MessageInput(onMessageSend: (String) -> Unit)
{

    var message by remember { mutableStateOf("")
    }
    Row(modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically)
    {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {
                message = it
            }
        )
        IconButton(onClick = {
            if(message.isNotEmpty()){
                onMessageSend(message)
                message = ""
            }
        })
        {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Send Message"
            )
        }
    }
}

@Composable
fun AppHeader()
{
    Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primary))
    {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Parking Attendant Chat Page",
            color = Color.White,
            fontSize = 20.sp
        )
    }
}