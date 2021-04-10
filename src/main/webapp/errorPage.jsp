<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <link href="styles/style.css" rel="stylesheet" type="text/css">
    <title>Error page</title>
</head>
<body>
<div class="mainBody">
<h2>Error code: 500</h2>
<br>
<br>
<br>
<ul>
    <li><a href=${pageContext.request.contextPath}/controller?command=main><fmt:message key="global.toMain"/></a></li>
</ul>
</div>
</body>
</html>
