
package com.example.aegismesh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF06151D)
private val CardColor = Color(0xFF0C2430)
private val Teal = Color(0xFF24D6B5)
private val Muted = Color(0xFF91AAB8)
private val White = Color(0xFFF2F8FA)
private val Red = Color(0xFFFF5265)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Teal,
                    background = Background,
                    surface = CardColor,
                    onPrimary = Background,
                    onBackground = White,
                    onSurface = White
                )
            ) {
                AegisMeshApp()
            }
        }
    }
}

private enum class Page {
    HOME, MESSAGES, DEVICES, CHAT
}

private data class Contact(
    val name: String,
    val initials: String,
    val message: String,
    val time: String,
    val online: Boolean
)

private data class ChatMessage(
    val text: String,
    val mine: Boolean
)

@Composable
private fun AegisMeshApp() {
    var page by remember { mutableStateOf(Page.HOME) }
    var selectedContact by remember { mutableStateOf("Riya") }

    val contacts = remember {
        listOf(
            Contact("Riya", "R", "Hey! Are you there?", "10:24 AM", true),
            Contact("Arjun", "A", "Got it. Stay safe.", "Yesterday", true),
            Contact("Team A", "T", "Let's meet at the main gate", "Yesterday", true),
            Contact("Group (5)", "G", "Emergency meeting at 4 PM", "Yesterday", false),
            Contact("Me", "M", "You: Alright!", "Yesterday", false)
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        Scaffold(
            containerColor = Background,
            bottomBar = {
                if (page != Page.CHAT) {
                    NavigationBar(
                        containerColor = Color(0xFF081C26),
                        contentColor = Teal
                    ) {
                        NavigationBarItem(
                            selected = page == Page.HOME,
                            onClick = { page = Page.HOME },
                            icon = {
                                Icon(Icons.Default.Home, contentDescription = "Home")
                            },
                            label = { Text("Home") },
                            colors = navColors()
                        )
                        NavigationBarItem(
                            selected = page == Page.MESSAGES,
                            onClick = { page = Page.MESSAGES },
                            icon = {
                                Icon(Icons.Default.Chat, contentDescription = "Messages")
                            },
                            label = { Text("Chats") },
                            colors = navColors()
                        )
                        NavigationBarItem(
                            selected = page == Page.DEVICES,
                            onClick = { page = Page.DEVICES },
                            icon = {
                                Icon(Icons.Default.Devices, contentDescription = "Devices")
                            },
                            label = { Text("Devices") },
                            colors = navColors()
                        )
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (page) {
                    Page.HOME -> HomeScreen(
                        contacts = contacts,
                        onMessages = { page = Page.MESSAGES },
                        onDevices = { page = Page.DEVICES },
                        onChat = {
                            selectedContact = it
                            page = Page.CHAT
                        }
                    )

                    Page.MESSAGES -> MessagesScreen(
                        contacts = contacts,
                        onChat = {
                            selectedContact = it
                            page = Page.CHAT
                        }
                    )

                    Page.DEVICES -> DevicesScreen()

                    Page.CHAT -> ChatScreen(
                        contactName = selectedContact,
                        onBack = { page = Page.MESSAGES }
                    )
                }
            }
        }
    }
}

@Composable
private fun navColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Teal,
    selectedTextColor = Teal,
    unselectedIconColor = Muted,
    unselectedTextColor = Muted,
    indicatorColor = Color(0xFF153B40)
)

// ---------------- HOME DASHBOARD ----------------

@Composable
private fun HomeScreen(
    contacts: List<Contact>,
    onMessages: () -> Unit,
    onDevices: () -> Unit,
    onChat: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 18.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "AegisMesh",
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Offline. Encrypted. Together.",
                    color = Muted,
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFF153B40), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Shield,
                    contentDescription = "Privacy",
                    tint = Teal,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        // Mesh status card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardColor, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Teal, CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Mesh Ready",
                    color = Teal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    "Offline UI demo • Not connected",
                    color = Muted,
                    fontSize = 12.sp
                )
            }
            Icon(
                Icons.Default.Bluetooth,
                contentDescription = "Bluetooth",
                tint = Teal,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(
            "Quick Actions",
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionCard(
                title = "Messages",
                subtitle = "Chat with people",
                icon = "chat",
                modifier = Modifier.weight(1f),
                onClick = onMessages
            )
            ActionCard(
                title = "Nearby Devices",
                subtitle = "Find peers",
                icon = "devices",
                modifier = Modifier.weight(1f),
                onClick = onDevices
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionCard(
                title = "Emergency SOS",
                subtitle = "Emergency alert",
                icon = "sos",
                modifier = Modifier.weight(1f),
                onClick = {
                    // SOS UI will be implemented later.
                },
                isEmergency = true
            )
            ActionCard(
                title = "Privacy",
                subtitle = "Secure messaging",
                icon = "shield",
                modifier = Modifier.weight(1f),
                onClick = {
                    // Privacy settings will be implemented later.
                }
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recent Chats",
                color = White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "See all",
                color = Teal,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onMessages() }
            )
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(contacts.take(3)) { contact ->
                ContactRow(contact = contact, onClick = {
                    onChat(contact.name)
                })
            }
        }
    }
}

@Composable
private fun ActionCard(
    title: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isEmergency: Boolean = false
) {
    val accent = if (isEmergency) Red else Teal

    Column(
        modifier = modifier
            .height(118.dp)
            .background(
                if (isEmergency) Color(0xFF291C29) else CardColor,
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalArrangement = Arrangement.Center
    ) {
        when (icon) {
            "chat" -> Icon(Icons.Default.Chat, null, tint = accent)
            "devices" -> Icon(Icons.Default.Devices, null, tint = accent)
            "sos" -> Icon(Icons.Default.Sos, null, tint = accent)
            else -> Icon(Icons.Default.Shield, null, tint = accent)
        }

        Spacer(Modifier.height(8.dp))

        Text(
            title,
            color = White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )
        Text(
            subtitle,
            color = Muted,
            fontSize = 10.sp
        )
    }
}

// ---------------- MESSAGES LIST ----------------

@Composable
private fun MessagesScreen(
    contacts: List<Contact>,
    onChat: (String) -> Unit
) {
    var search by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 18.dp)
    ) {
        Spacer(Modifier.height(22.dp))

        Text(
            "Messages",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardColor, RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, null, tint = Muted)
            Spacer(Modifier.width(10.dp))

            BasicTextField(
                value = search,
                onValueChange = { search = it },
                singleLine = true,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = White,
                    fontSize = 14.sp
                ),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    Box {
                        if (search.isEmpty()) {
                            Text("Search chats...", color = Muted, fontSize = 14.sp)
                        }
                        innerTextField()
                    }
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(
                contacts.filter {
                    it.name.contains(search, ignoreCase = true)
                }
            ) { contact ->
                ContactRow(contact = contact, onClick = {
                    onChat(contact.name)
                })
            }
        }
    }
}

@Composable
private fun ContactRow(
    contact: Contact,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF294351), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                contact.initials,
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                contact.name,
                color = White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                contact.message,
                color = Muted,
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(contact.time, color = Muted, fontSize = 10.sp)
            Spacer(Modifier.height(8.dp))
            if (contact.online) {
                Box(
                    Modifier
                        .size(7.dp)
                        .background(Teal, CircleShape)
                )
            }
        }
    }

    HorizontalDivider(color = Color(0xFF17313C), thickness = 0.5.dp)
}

// ---------------- INDIVIDUAL CHAT ----------------

@Composable
private fun ChatScreen(
    contactName: String,
    onBack: () -> Unit
) {
    val messages = remember(contactName) {
        mutableStateListOf(
            ChatMessage("Hey! Are you there?", true),
            ChatMessage("Yes! I'm at the shelter now. Everything is safe here.", false),
            ChatMessage("Great! Let me know if you need anything.", true),
            ChatMessage("Will do! 👍", false)
        )
    }

    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF081C26))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = White,
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onBack() }
            )

            Spacer(Modifier.width(14.dp))

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color(0xFF294351), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    contactName.take(1).uppercase(),
                    color = White,
                    fontSize = 18.sp
                )
            }

            Spacer(Modifier.width(10.dp))

            Column {
                Text(
                    contactName,
                    color = White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text("Offline chat demo", color = Teal, fontSize = 11.sp)
            }
        }

        Text(
            "Messages are sample UI content",
            color = Muted,
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(messages) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (message.mine) {
                        Arrangement.End
                    } else {
                        Arrangement.Start
                    }
                ) {
                    Text(
                        text = message.text,
                        color = White,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier
                            .widthIn(max = 290.dp)
                            .background(
                                if (message.mine) Color(0xFF087F73)
                                else CardColor,
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 11.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF081C26))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .background(CardColor, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = White,
                    fontSize = 14.sp
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box {
                        if (messageText.isEmpty()) {
                            Text("Type a message...", color = Muted, fontSize = 14.sp)
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        messages.add(ChatMessage(messageText.trim(), true))
                        messageText = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(Teal, CircleShape)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send message",
                    tint = Background
                )
            }
        }
    }
}

// ---------------- NEARBY DEVICES ----------------

@Composable
private fun DevicesScreen() {
    val devices = listOf(
        Contact("Arjun's Phone", "A", "Nearby • Signal: Strong", "", true),
        Contact("Team Laptop", "T", "Nearby • Signal: Medium", "", true),
        Contact("Riya's Phone", "R", "Nearby • Signal: Weak", "", false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 18.dp)
    ) {
        Spacer(Modifier.height(22.dp))

        Text(
            "Nearby Devices",
            color = White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            "Discover devices around you",
            color = Muted,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(160.dp)
                    .background(Color(0xFF0A292F), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .size(112.dp)
                        .background(Color(0xFF104044), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        Modifier
                            .size(64.dp)
                            .background(Color(0xFF165652), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Bluetooth,
                            contentDescription = null,
                            tint = Teal,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        Text(
            "3 sample devices",
            color = Teal,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            "Real device discovery will be added later",
            color = Muted,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 5.dp, bottom = 20.dp)
        )

        Text(
            "Available Devices",
            color = White,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(10.dp))

        devices.forEach { device ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(CardColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Devices,
                        contentDescription = null,
                        tint = Teal
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        device.name,
                        color = White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(device.message, color = Muted, fontSize = 11.sp)
                }

                Box(
                    Modifier
                        .size(8.dp)
                        .background(
                            if (device.online) Teal else Muted,
                            CircleShape
                        )
                )
            }

            HorizontalDivider(color = Color(0xFF17313C), thickness = 0.5.dp)
        }
    }
}