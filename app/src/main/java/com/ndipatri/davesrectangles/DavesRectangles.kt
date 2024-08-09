package com.ndipatri.davesrectangles

import android.content.res.Resources
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
 *
 * My solution is to use the Preview feature of Compose and the graphic
 * rendering system to provide us a visual test harness... a bit goofy,
 * but the visual feedback is very fast as you iterate through implementing
 * the 'columnar' target function... the alternative testing approach would be to manually
 * calculate the 'expected' returned child origins. These expected value calculations
 * would be time consuming.. Especially for large numbers of columns... so for an interview
 * situation, my Preview test functions is useful choice.
 *
 * To solve this, I just started with the default 'Compose' sample project in Android Studio
 * and created this 'DavesRectangles' file with Composables in it...
 *
 * My 'mock interview' took me 25 minute to write the test harness, then about 35
 * to implement fully with only one or two Preview tests written .. and there was only
 * one minor bug.
 *
 * During my interview, I disabled CoPilot and Gemini and I did Google 'Compose Canvas draw rectangle' to remember the 'drawRect' function.
 *
 * For an interview candidate who does NOT write any tests, they will
 * probably not produce a bug-free solution within an hour. They should be warned of this
 * ahead of time in order to prevent super-stressing out the candidate.
 *
 * My initial pass, which took a little over an hour, has a few minor bugs. Subsequent commits added
 * tests and fixed more bugs (these two activities were correlated :-)
 */

class Rectangle(val width: Int, val height: Int)

@Preview(showBackground = true)
@Composable
fun SameWidthParentAndChildren() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(500, 500)
    val children = listOf(
        Rectangle(500, 100),
        Rectangle(500, 100),
        Rectangle(500, 100),
        Rectangle(500, 100),
        Rectangle(500, 100),
    )
    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun SameHeightParentAndChildren() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(500, 500)
    val children = listOf(
        Rectangle(100, 500),
        Rectangle(100, 500),
        Rectangle(100, 500),
        Rectangle(100, 500),
        Rectangle(100, 500),
    )
    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun ManyColumnsManyRows() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(500, 500)
    val children = listOf(
        Rectangle(20, 50),
        Rectangle(50, 20),
        Rectangle(10, 30),
        Rectangle(20, 100),
        Rectangle(200, 500),
        Rectangle(100, 500),
    )
    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun ManyColumnsManyRows2() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(400, 400)
    val children = listOf(
        Rectangle(20, 50),
        Rectangle(50, 20),
        Rectangle(10, 30),
        Rectangle(20, 100),
        Rectangle(200, 100),
        Rectangle(100, 350),
    )
    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun ManyColumnsManyRows3() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(400, 400)
    val children = listOf(
        Rectangle(20, 50),
        Rectangle(50, 20),
        Rectangle(10, 30),
        Rectangle(20, 100),
        Rectangle(200, 100),
        Rectangle(200, 350),
    )
    RectangleOfRectangles(parent, children)
}

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
fun StrangeParent() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(200, 500)
    val children = listOf(
        Rectangle(100, 100),
        Rectangle(100, 100),
        Rectangle(100, 100),
        Rectangle(100, 100),
        Rectangle(100, 50),
        Rectangle(100, 50),
        Rectangle(100, 20),
        Rectangle(100, 20),
    )

    RectangleOfRectangles(parent, children)
}

@Preview(showBackground = true)
@Composable
fun NormalChildren() {

    // input: parent rectangle and a list of small rectangles...
    val parent = Rectangle(500, 500)
    val children = listOf(
        Rectangle(100, 100),
        Rectangle(300, 100),
        Rectangle(100, 100),
        Rectangle(100, 100),
        Rectangle(100, 50),
        Rectangle(100, 100),
        Rectangle(100, 50),
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
    ) {
        Text(children.map { child -> "${child.width} x ${child.height}\n" }.toString())
    }
}

// Idea is to layout children within the parent rectangle....
fun columnar(parent: Rectangle, children: List<Rectangle>): List<Offset> {

    // The list of offsets we are returning
    var offsets = mutableListOf<Offset>()

    var columnIndex = 0

    // we need to track how much width we've consumed so far
    var columnWidths = mutableListOf(0)

    // need this to keep track of WHEN we start next column
    var currentColumnHeight = 0

    // need this to keep track of WHERE we start next column
    var currentColumnWidth = 0

    children.forEach { child ->
        // changes as we build columns
        val availableWidth = parent.width - columnWidths.sum()

        // first we see if view can fit in current column
        if (child.width > availableWidth) {
            return offsets
        }
        if (child.height + currentColumnHeight > parent.height) {
            // child cannot fit in current column!

            // can it fix it a new column?
            if (child.height > parent.height ||
                child.width > availableWidth - currentColumnWidth) {
                return offsets
            }

            // child can fit in a new column, so
            // advance to new column
            columnIndex += 1
            columnWidths.add(currentColumnWidth)
            currentColumnWidth = 0
            currentColumnHeight = 0
        }

        // column is wider than widest child
        if (child.width > currentColumnWidth) currentColumnWidth = child.width

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
private fun RectangleOfRectanglesContent(parent: Rectangle2D, children: List<Rectangle2D>, content: @Composable () -> Unit) {
    RectangleContent(parent)
    children.forEach { child ->
        RectangleContent(child, Color.Red)
    }
    content.invoke()
}

@Composable
private fun RectangleContent(rectangle: Rectangle2D, color: Color = Color.Blue) {
    Card(modifier = Modifier
        .width(rectangle.width.dp / Resources.getSystem().displayMetrics.density)
        .height(rectangle.height.dp / Resources.getSystem().displayMetrics.density)
        .absoluteOffset(
            rectangle.origin.x.dp / Resources.getSystem().displayMetrics.density,
            rectangle.origin.y.dp / Resources.getSystem().displayMetrics.density
        )
        .border(BorderStroke(1.dp, color))) {}
}