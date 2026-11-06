package com.example.magpie_pinecone.frontend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignupScreen(modifier: Modifier = Modifier, onClickSignUp: () -> Unit) {
    var firstName by remember {mutableStateOf("")}
    var lastName by remember {mutableStateOf("")}
    var email by remember {mutableStateOf("")}
    var phone by remember {mutableStateOf("")}
    var isOwner by remember {mutableStateOf(false)}

    Column(modifier = Modifier.fillMaxSize().padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(36.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally) {
        // Left aligned text
        Text("Create Profile", modifier = Modifier.align(Alignment.Start), fontSize = 36.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(20.dp))
        
        // Input fields
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            placeholder = { Text("Enter your first name...") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            placeholder = { Text("Enter your last name...") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            placeholder = { Text("Enter your email...") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            placeholder = { Text("Enter your phone number...") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Entrant")
            Switch(
                checked = isOwner,
                onCheckedChange = {isOwner = !isOwner}
            )
            Text("Owner")
        }

        //Spacer(modifier = Modifier.height(20.dp))
        Button(modifier = Modifier.size(width = 180.dp, height = 60.dp),
            onClick = onClickSignUp,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6155f5),
                contentColor = Color.White
            ), shape = RoundedCornerShape(8.dp)
        ) {
            Text("Sign up", fontSize = 20.sp)
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun SignupScreenPreview() {
    SignupScreen(onClickSignUp = {})
}