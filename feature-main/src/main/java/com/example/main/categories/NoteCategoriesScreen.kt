package com.example.main.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.navigation.BaseNavigator
import com.example.core.navigation.MockNavigator
import com.example.main.model.NoteCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCategoriesScreen(
    navigator: BaseNavigator = MockNavigator
) {
    val categories = listOf(
        NoteCategory(1, "Личные расходы"),
        NoteCategory(2, "Коммунальные платежи"),
        NoteCategory(3, "Рабочие заметки"),
        NoteCategory(4, "Покупки")
    )
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Категории заметок") },
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Назад",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(paddingValues).padding(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                NoteCategoryItem(category) {

                }
            }
        }
    }

}


@Composable
fun NoteCategoryItem(category: NoteCategory, onClick: (NoteCategory) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(category) },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Default.Folder, contentDescription = "Category Icon")
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = category.name, fontSize = 18.sp, fontWeight = FontWeight.Medium)
        }
    }
}