package com.ndipatri.davesrectangles

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.min

/**
 * The Problem:
 *
 *
 * Lays out a list of "views" within a "screen"
 *
 * Views are to be laid out vertically in order, top to bottom, left justified. When a view cannot
 * be laid out in the remaining vertical space, it starts a new column, left-justified to the right
 * edge of the widest view in the previous column
 *
 * Here is the function you need to write:
 *
 * @param screen the overall available area
 * @param views the rectangles to be laid out in that space
 * @return the coordinates of the top-left corner of each of the laid out views
 *
 *  fun columnar(screen: Rectangle, views: List<Rectangle>): List<Point> {
 *       TODO()
 *  }
 **/

/**
 * The Solution:
 *
 * The idea here is we use the Preview feature of Compose and the graphic
 * rendering system to provide us a visual test harness... a bit goofy,
 * but the visual feedback is very fast as you iterate through implementing
 * the 'column' target function.
 *
 * To solve this, I just started with the default 'Compose' sample project in Android Studio
 * and created this DavesRectangle file with Composables in it...
 *
 * The initial pass at this took me 25 minute to write the test harness, then about 35
 * to implement fully with only one or two Preview tests written .. so 5 minutes over.. I'M FIRED! :-)
 *
 * My initial pass which took a little of an hour is the 'initial commit' to this repo.. Subsequent
 * commits were cleanup as cosmetic improvements.
 */

class Rectangle(val width: Int, val height: Int)

@Preview(showBackground = true)
@Composable
fun SingleChild() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(500, 500)
    val children = listOf(
        Rectangle(100, 100),
    )
    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun EmptyChildren() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(500, 500)
    val children: List<Rectangle> = listOf()
    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun OneStupidBigChild() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(500, 500)
    val children = listOf(
        Rectangle(10000, 10000),
    )
    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun EqualParentAndChild() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(500, 500)
    val children = listOf(
        Rectangle(500, 500),
    )
    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun RectangleWithTooManyChildren() {

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

        // these aren't rendered
        Rectangle(100, 700),
        Rectangle(100, 100),
    )

    RectangleOfRectangles(parent, children)
}

@Composable
private fun RectangleOfRectangles(
    parent: Rectangle,
    children: List<Rectangle>
) {
    val offsets = columnar(parent, children)

    // now we test ...
    RectangleOfRectanglesContent(
        parent = Rectangle2D(parent.width, parent.height, Offset(0F, 0F)),
        children = children.subList(0, offsets.size).mapIndexed { index, child ->
            Rectangle2D(child.width, child.height, offsets[min(index, offsets.size - 1)])
        }
    )
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
        if (child.height > availableWidth) return offsets

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
        offsets.add(
            Offset(
                columnWidths.sum().toFloat(),
                currentColumnHeight.toFloat()
            )
        )

        // add child to this column
        currentColumnHeight += child.height
    }

    return offsets
}

class Rectangle2D(val width: Int, val height: Int, val origin: Offset)

@Composable
private fun RectangleOfRectanglesContent(parent: Rectangle2D, children: List<Rectangle2D>) {
    RectangleContent(parent)
    children.forEach { child ->
        RectangleContent(child, Color.Red)
    }
}

// Draw Rectangle Googled from here:
// https://nameisjayant.medium.com/draw-rectangle-with-canvas-in-jetpack-compose-539d1890ddd2
@Composable
private fun RectangleContent(rectangle: Rectangle2D, color: Color = Color.Blue) {
    Canvas(
        modifier = Modifier
            .width(rectangle.width.dp / Resources.getSystem().displayMetrics.density)
            .height(rectangle.height.dp / Resources.getSystem().displayMetrics.density)
    ) {
        drawRect(
            style = Stroke(width = 10f),
            color = color,
            size = Size(rectangle.width.toFloat(), rectangle.height.toFloat()),
            topLeft = rectangle.origin
        )
    }
}