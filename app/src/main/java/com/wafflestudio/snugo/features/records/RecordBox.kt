package com.wafflestudio.snugo.features.records

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.wafflestudio.snugo.models.Record
import java.text.SimpleDateFormat
import java.util.Date

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    return format.format(date)
}

fun durationToString(duration: Long): String {
    val second = (duration / 1000) % 60
    var minute = (duration / 1000) / 60
    val hour = minute / 60
    minute %= 60
    return if (hour == 0L) {
        if (minute == 0L) {
            second.toString() + "초"
        } else {
            minute.toString() + "분 " + second.toString() + "초"
        }
    } else {
        hour.toString() + "시간 " +
            minute.toString() + "분 " +
            second.toString() + "초"
    }
}

@Composable
fun RecordBox(
    modifier: Modifier = Modifier,
    record: Record,
    navController: NavController,
    boxClicked: () -> Unit,
) {
    Log.d("aaaa", "box")
    Column(
        modifier =
            Modifier.padding(12.dp)
                .fillMaxWidth().border(2.dp, Color.Cyan, RoundedCornerShape(10.dp))
                .clickable {
                    boxClicked()
                    // navController.navigate(NavigationDestination.RecordMap.route)
                },
    ) {
        val duration = durationToString(record.duration)
        Text(
            text = record.nickname,
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            modifier = Modifier.height(100.dp),
        ) {
            Text(
                duration,
                fontSize = 30.sp,
                modifier =
                    Modifier.padding(15.dp).weight(1f)
                        .align(Alignment.CenterVertically),
            )
            record.route.buildings.mapIndexed { index, building ->
                var addText = "-> "
                if (index == record.route.buildings.size) {
                    addText = ""
                }
                Text(
                    building.id,
                    fontWeight = FontWeight.ExtraBold,
                    modifier =
                        Modifier.padding(15.dp).weight(1.5f)
                            .align(Alignment.CenterVertically),
                )
            }
        }
    }
}
