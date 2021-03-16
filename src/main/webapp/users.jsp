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
    <title><fmt:message key="users.title"/></title>
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
<table border="1" width="600">
    <tr bgcolor="00FF7F">
        <th><b><fmt:message key="global.id"/></b></th>
        <th><b><fmt:message key="profile.role"/></b></th>
        <th><b><fmt:message key="login.title"/></b></th>
        <th><b><fmt:message key="profile.balance"/></b></th>
        <th><b><fmt:message key="users.banned"/></b></th>
    </tr>
    <c:forEach var="user" items="${requestScope.allUsers}">
        <tr>
            <td>${user.id}</td>
            <c:choose>
                <c:when test="${user.roleId==1}">
                    <td><fmt:message key="users.admin"/></td>
                </c:when>
                <c:otherwise>
                    <td><fmt:message key="users.user"/></td>
                </c:otherwise>
            </c:choose>
            <td>${user.login}</td>
            <td>${user.balance}</td>
            <td>${user.banned}</td>
        </tr>
    </c:forEach>
</table>
<br>
<br>
<h3><fmt:message key="users.operation"/>:</h3>
<form action="${pageContext.request.contextPath}/controller?command=change_user_information" method="post">
    <fmt:message key="users.user"/>: <select name="selectedUser">
    <jsp:useBean id="users" scope="request" type="java.util.List"/>
    <option selected="selected">Please Select</option>
    <c:forEach items="${users}" var="users">
        <option value="${users.login}">${users.login}</option>
    </c:forEach></select>
    <fmt:message key="users.option"/>: <select name="selectedOption">
    <option value="ban"><fmt:message key="users.operation.ban"/></option>
    <option value="promote"><fmt:message key="users.operation.promote"/></option>
</select>
    <input type="submit" value="<fmt:message key="global.submit"/>">
</form>
<br>
<br>
<br>
<a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a>
</body>
</html>
