package eo.view.batterymeter.shapes

import org.apache.batik.anim.dom.SAXSVGDocumentFactory
import org.apache.batik.parser.PathParser
import org.apache.batik.util.XMLResourceDescriptor
import org.w3c.dom.svg.SVGDocument
import org.w3c.dom.svg.SVGElement


fun loadSvgDocument(filePath: String): SVGDocument {
    val parser = XMLResourceDescriptor.getXMLParserClassName()
    val documentFactory = SAXSVGDocumentFactory(parser)

    return documentFactory.createSVGDocument(filePath)
}

fun extractViewBox(svgElement: SVGElement): ViewBox {
    val viewBoxStr = svgElement.attributes.getNamedItem("viewBox").nodeValue
    return ViewBox.fromString(viewBoxStr)
}

fun extractPathData(svgElement: SVGElement): String {
    val pathElement = svgElement.getElementsByTagName("path").item(0)
    return pathElement.attributes.getNamedItem("d").nodeValue
}

fun parsePathData(pathData: String): List<PathCommand> {
    val pathDataHandler = PathDataHandler()

    with(PathParser()) {
        pathHandler = pathDataHandler
        parse(pathData)
    }

    return pathDataHandler.pathCommands
}