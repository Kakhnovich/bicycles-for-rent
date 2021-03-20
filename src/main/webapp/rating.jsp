<%@ page import="java.util.Map" %>
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
    <title><fmt:message key="rating.title"/></title>
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
<h2><fmt:message key="rating.msg.byOrders"/></h2>
<table border="1" width="400">
    <tr bgcolor="00FF7F">
        <th><b><fmt:message key="rating.login"/></b></th>
        <th><b><fmt:message key="rating.ordersCount"/></b></th>
    </tr>
    <c:forEach items="${rating}" var="entry1">
        <tr>
            <td>${entry1.key}</td>
            <td>${entry1.value}</td>
        </tr>
    </c:forEach>
</table>
<br>
<h2><fmt:message key="rating.msg.byHours"/></h2>
<table border="1" width="400">
    <tr bgcolor="00FF7F">
        <th><b><fmt:message key="rating.login"/></b></th>
        <th><b><fmt:message key="rating.hoursCount"/></b></th>
    </tr>
    <c:forEach items="${hoursRating}" var="entry2">
        <tr>
            <td>${entry2.key}</td>
            <td>${entry2.value}</td>
        </tr>
    </c:forEach>
</table>
<br>
<br>
<br>
<ul>
    <li><a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a></li>
</ul>
<jsp:include page="/commands.jsp"/>
</body>
</html>
