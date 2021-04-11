<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
		<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

			<!DOCTYPE>
			<html>

			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
				<title>Web Crawler Results</title>
				<link rel="stylesheet" href="css/displaytag.css" type="text/css">
				<link rel="stylesheet" href="css/screen.css" type="text/css">
				<link href="webjars/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
				<link rel="stylesheet" href="css/display.css" type="text/css">
			</head>

			<body>
				<caption>
					<h3>Result:</h3>
				</caption>
				<hr><br>
				<div id='tab1' class="tab_content" style="display: block; width: 100%">
					<h3>Displays crawled results with sorting and pagination</h3>
					

					<display:table name="list" pagesize="50" export="true" sort="list" requestURI="/userAction.do" uid="DataModelExcel">
						<display:column property="title" title="Title" sortable="true" headerClass="sortable" />
						<display:column property="description" title="Description" sortable="true"
							headerClass="sortable" />
						<display:column property="robots" title="Robots" sortable="true" headerClass="sortable" />
						<display:column property="url" title="Url" sortable="true" headerClass="sortable" />
					</display:table>
				</div>

				<script src="webjars/jquery/2.2.4/jquery.min.js"></script>
				<script src="webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>

			</body>

			</html>