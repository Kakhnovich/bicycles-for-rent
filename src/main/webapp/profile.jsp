<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title><fmt:message key="profile.title"/></title>
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
<h2><fmt:message key="profile.msg"/></h2>
<h3><fmt:message key="login.title"/> - ${user.login}</h3>
<h3><fmt:message key="profile.balance"/> - ${user.balance}</h3>
<h5><fmt:message key="profile.operation.changeBalance"/>
    <form action="${pageContext.request.contextPath}/controller?command=change_balance" method="post">
        <input type="number" name="money" min="1">
        <input type="submit">
    </form>
</h5>
<h5>(<fmt:message key="profile.operation.changeBalance.msg"/>)</h5>
<h3><fmt:message key="profile.role"/> - ${user.role}</h3>
<c:if test="${not empty requestScope.orders}">
    <h3><fmt:message key="orders.title"/>:</h3>
    <table border="1" width="600">
        <tr bgcolor="00FF7F">
            <th><b><fmt:message key="bicycles.model"/></b></th>
            <th><b><fmt:message key="orders.hours"/></b></th>
            <th><b><fmt:message key="orders.status"/></b></th>
            <th><b><fmt:message key="orders.date"/></b></th>
        </tr>
        <c:forEach var="orders" items="${requestScope.orders}">
            <tr>
                <td>${orders.bicycleModel}</td>
                <td>${orders.hours}</td>
                <td>${orders.status}</td>
                <td>${orders.date}</td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<br>
<br>
<br>
<a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a>
</body>
</html>
