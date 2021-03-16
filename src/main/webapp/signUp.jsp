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
    <title><fmt:message key="signUp.title"/></title>
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
<h4><fmt:message key="signUp.msg"/></h4>
<form action="${pageContext.request.contextPath}/controller?command=sign_up" method="post">
    <input name="userName" placeholder="<fmt:message key="login.title"/>">
    <input type="password" name="userPassword" placeholder="<fmt:message key="login.userPassword"/>">
    <input type="submit" value="<fmt:message key="global.submit"/>">
</form>
<br>
<h4><fmt:message key="signUp.toLogin"/>? <a
        href=${pageContext.request.contextPath}/controller?command=login><fmt:message
        key="mainPage.toLogin"/></a></h4>
<br>
<br>
<br>
<a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a>
</body>
</html>
