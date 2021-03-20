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
    <title><fmt:message key="mainPage.title"/></title>
</head>
<body>
<form action="${pageContext.request.contextPath}/controller?command=change_locale" method="post">
    <fmt:message key="global.locale"/>: <select name="locale">
    <option value="en"><fmt:message key="global.en"/></option>
    <option value="ru"><fmt:message key="global.ru"/></option>
    <option value="cn"><fmt:message key="global.cn"/></option>
</select>
    <input type="submit" value="<fmt:message key="global.submit"/>">
</form>
<h2><fmt:message key="mainPage.hello1"/></h2>
<h4>Our places:</h4>
<c:forEach var="place" items="${requestScope.places}">
    <form action="${pageContext.request.contextPath}/controller?command=order_page&place=${place}" method="post">
        <h5>${place}</h5>
        <h5>Chose date - <input name="date" type="date" value=""></h5>
<%--        todo check--%>
        <input type="submit" value="see available bicycles">
    </form>
    <br>
</c:forEach>
<jsp:include page="/commands.jsp" />
</body>
</html>