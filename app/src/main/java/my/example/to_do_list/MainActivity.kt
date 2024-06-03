package my.example.to_do_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import my.example.to_do_list.ui.theme.TODOLISTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOLISTTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    To_Do_ListAPP()
                }
            }
        }
    }
}

data class ToDoTask(
    val id : Int,
    var task: String,
    var time: String,
    var isCompleted: Boolean = false
)



@Composable
fun To_Do_ListAPP(){

    var Tasks by remember {
        mutableStateOf(listOf<ToDoTask>())
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var taskName by remember {
        mutableStateOf("")
    }
    var TaskTime by remember {
        mutableStateOf("")
    }

//    var sItem by remember {
//        mutableStateOf(listOf<SItemDATA>())
//    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.hsv(187f, 0.12f, 0.99f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.TopStart),
            Arrangement.SpaceAround
        ) {
            val itemList: List<String> = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(Tasks){
                    task ->
                    ToDoTaskList(
                        task = task,
                        onCompleteChange = { isCompleted ->
                            Tasks = Tasks.map {
                                if (it.id == task.id) it.copy(isCompleted = isCompleted)
                                else it
                            }
                        },
                        DO_I_Delete= {
                            Tasks = Tasks.filter { it.id != task.id }
                        }
                    )


                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement =  Arrangement.Center,


        ) {


            Spacer(modifier = Modifier.padding(bottom = 32.dp))
            val buttonBackgroundColor = hexToComposeColor("#073b4c")
            val buttonContentColor = Color.White
            Button(
                onClick = { showDialog = true },
                colors = ButtonDefaults.buttonColors(
                    contentColor = buttonContentColor,
                    containerColor = buttonBackgroundColor as Color
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }

    fun hexToComposeColor(hexColor:String): Color {
        val colorInt = hexColor.removePrefix("#").toLong(radix = 16).toInt() or 0xFF000000.toInt()
        return Color(colorInt)
    }

    if( showDialog){

        Column {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement =  Arrangement.SpaceBetween) {
                        Button(
                            onClick = {
                                if (taskName.isNotBlank()){
                                    val newTask = ToDoTask(
                                        id = Tasks.size + 1,
                                        task = taskName,
                                        time = TaskTime
                                    )
                                    Tasks = Tasks + newTask
                                    showDialog = false
                                    taskName = ""
                                    TaskTime = ""
                                }else{
                                    showDialog = false
                                }
                            })
                        {
                            Text(text = "Add")
                        }

                        //Cancel Button
                        Button(onClick = { showDialog = false}) {
                            Text("cancel")
                        }
                    }
                },
                title = { Text(text = "Add To Do Task")},
                text ={
                    Column {
                        Text(text = "task")
                        OutlinedTextField(
                            value = taskName,
                            onValueChange = {taskName = it},
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        Text(text = "time?")
                        OutlinedTextField(
                            value = TaskTime,
                            onValueChange = {TaskTime = it},
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



}

fun hexToComposeColor(s: String): Any {
    val colorInt = s.removePrefix("#").toLong(radix = 16).toInt() or 0xFF000000.toInt()
    return Color(colorInt)
}


////end of app

@Composable
fun ToDoTaskList(
    task: ToDoTask,
    onCompleteChange: (Boolean)-> Unit,
    DO_I_Delete: ()-> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .padding(0.dp)
            .background(Color.hsv(12f, 0.68f, 0.93f)),
        verticalAlignment = Alignment.CenterVertically,


    ) {
        Checkbox(
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Transparent, // Color when checked
                uncheckedColor = Color.Black // Color when unchecked
            ),
            checked = task.isCompleted,
            onCheckedChange = { isChecked ->
                onCompleteChange(isChecked)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Row (horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
            Text(
                text = task.task,
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
                    .padding(end = 8.dp)


                    //////


                    .then(Modifier.drawWithContent {
                        // Draw a line through the text when it's completed
                        if (task.isCompleted) {
                            drawContent()
                            drawLine(
                                color = Color.Black,
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 2f
                            )
                        } else {
                            drawContent()
                        }
                    }),



                //////
//            .textDecoration(if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None),
                color = if (task.isCompleted) Color.hsv(12f, 0.68f, 0.30f) else Color.Black
            )
            Text(
                text = task.time,
                modifier = Modifier.weight(1f)
                .then(Modifier.drawWithContent {
                // Draw a line through the text when it's completed
                if (task.isCompleted) {
                    drawContent()
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 2f
                    )
                } else {
                    drawContent()
                }
            }),
                color = if (task.isCompleted) Color.hsv(12f, 0.68f, 0.30f) else Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = DO_I_Delete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null,tint = Color.Black)
            }
        }


    }
}





@Preview(showBackground = true)
@Composable
fun FUNPreview() {
    TODOLISTTheme {
        To_Do_ListAPP()
    }
}
