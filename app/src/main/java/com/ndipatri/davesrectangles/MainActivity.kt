package com.ndipatri.davesrectangles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import com.ndipatri.davesrectangles.ui.theme.DavesRectanglesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DavesRectanglesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


class Rectangle(val width: Int, val height: Int)
@Preview(showBackground = true)
@Composable
fun RectangleWithChildren() {
    DavesRectanglesTheme {

        // input: parent rectangle and a list of small rectangles...
        val parent = Rectangle(500, 500)
        val children = listOf(
            Rectangle(100, 100),
            Rectangle(300, 100),
            Rectangle(100, 100),
            Rectangle(100, 100),
            Rectangle(100, 100),
            Rectangle(100, 100),
            Rectangle(100, 100),
            Rectangle(100, 100),
            Rectangle(100, 700),
        )

        // This is where we do our work (took 35 minute to get here)
        val offsets = columnar(parent, children)

        // now we test ...
        RectangleOfRectangles(
            parent = Rectangle2D(parent.width, parent.height, Offset(0F,0F)),
            children = children.mapIndexed { index, child ->
                Rectangle2D(child.width, child.height, offsets[index])
            }
        )
    }
}

// Idea is to layout children within the parent rectangle....
// this took about 45 minutes....
fun columnar(parent: Rectangle, children: List<Rectangle>): List<Offset> {

    var offsets = mutableListOf<Offset>()

    var columnIndex = 0
    var columnWidths = mutableListOf<Int>(0)

    var currentColumnHeight = 0
    var currentColumnWidth = 0

    var availableWidth = parent.width
    var availableHeight = parent.height

    children.forEach { child ->
        // as soon as we encounter a child that can no longer fit, we return what we have
        if (child.width > availableWidth) return offsets

        // column is as wide as widest child
        if (child.width > currentColumnWidth) currentColumnWidth = child.width

        if (child.height + currentColumnHeight > availableHeight) {
            // new column!
            columnIndex += 1
            columnWidths.add(currentColumnWidth)
            currentColumnWidth = 0
            currentColumnHeight = 0
        }

        // child can fit in current column...

        // Now that current column width and column height are updated
        // we can place child.
        offsets.add(Offset(columnWidths.sum().toFloat(),
                           currentColumnHeight.toFloat()))

        // add child to this column
        currentColumnHeight += child.height
    }

    return offsets
}

class Rectangle2D(val width: Int, val height: Int, val origin: Offset)
@Composable
private fun RectangleOfRectangles(parent: Rectangle2D, children: List<Rectangle2D>) {
    RectangleContent(parent)
    children.forEach { child ->
        RectangleContent(child)
    }
}

// Draw Rectangle Googled from here:
// https://nameisjayant.medium.com/draw-rectangle-with-canvas-in-jetpack-compose-539d1890ddd2
// this took about 20 minutes
@Composable
private fun RectangleContent(rectangle: Rectangle2D, color: Color = Color.Blue) {
    Canvas(
        modifier = Modifier
            //.padding(horizontal = 10.dp)
            .fillMaxSize()
    ) {
        drawRect(
            style = Stroke(width = 10f),
            color = color,
            size = Size(rectangle.width.toFloat(), rectangle.height.toFloat()),
            topLeft = rectangle.origin
        )
    }
}