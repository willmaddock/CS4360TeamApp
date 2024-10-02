package com.example.cs4360app.chatbots

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs4360app.R
import com.example.cs4360app.models.ChatViewModel
import com.example.cs4360app.models.MessageModel

@Composable
fun ChatPage(modifier: Modifier = Modifier, viewModel: ChatViewModel)
{
    Column(modifier = modifier) {
        AppHeader()
        MessageList(modifier = Modifier.weight(1f),messageList = viewModel.messageList)
        MessageInput(onMessageSend = {
            viewModel.sendMessage(it)
        })
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