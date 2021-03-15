<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<c:if test="${user.role!='user'}">
    <jsp:forward page="main.jsp"/>
</c:if>
<html>
<head>
    <title><fmt:message key="newOrder.title"/></title>
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
<h2><fmt:message key="newOrder.msg"/></h2>
<form action="${pageContext.request.contextPath}/controller?command=new_order" method="post">
    <h3><fmt:message key="bicycles.place"/>:
        <select name="orderPlace">
            <jsp:useBean id="places" scope="request" type="java.util.List"/>
            <option selected="selected"><fmt:message key="global.select"/></option>
            <c:forEach items="${places}" var="place">
                <option value="${place}">${place}</option>
            </c:forEach>
        </select></h3>
    <h3><fmt:message key="bicycles.model"/>:
        <select name="orderModel">
            <jsp:useBean id="models" scope="request" type="java.util.List"/>
            <option selected="selected"><fmt:message key="global.select"/></option>
            <c:forEach items="${models}" var="model">
                <option value="${model}">${model}</option>
            </c:forEach>
        </select></h3>
    <h3><fmt:message key="orders.date"/>: <input name="date" type="date"></h3>
    <h3><fmt:message key="newOrder.hours"/>: <input name="hours" type="number" min=1 max=24></h3>
    <input type="submit" value="<fmt:message key="global.submit"/>">
</form>
<br>
<br>
<br>
<a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a>
</body>
</html>
