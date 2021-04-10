<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<html>
<head>
    <link href="styles/style.css" rel="stylesheet">
    <title><fmt:message key="mainPage.title"/></title>
</head>
<body>
<jsp:include page="/commands.jsp"/>
<div class="mainBody">
    <h1><fmt:message key="mainPage.hello1"/></h1>
    <h2><fmt:message key="mainPage.msg"/>:</h2>
    <c:forEach var="place" items="${requestScope.places}">
        <form class="forms"
              action="${pageContext.request.contextPath}/controller?command=order_page&place=${place.address}"
              method="post">
            <div><b>${place.address}</b></div>
            <div><fmt:message key="mainPage.open"/> <i>${place.openTime}</i> <fmt:message key="mainPage.close"/>
                <i>${place.closeTime}</i></div>
            <div><fmt:message key="mainPage.chose"/> <input class="date" name="date" type="date" value=""></div>
            <div><input class="submit" type="submit" value="<fmt:message key="mainPage.button"/>"></div>
        </form>
        <br>
    </c:forEach>
</div>
</body>
</html>