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
<ul>
    <c:choose>
        <c:when test="${user.login==null}">
            <h2><fmt:message key="mainPage.hello1"/></h2>
            <h2><fmt:message key="mainPage.hello2"/></h2>
            <li><a href=${pageContext.request.contextPath}/controller?command=login><fmt:message
                    key="mainPage.toLogin"/></a></li>
            <li><a href=${pageContext.request.contextPath}/controller?command=sign_up><fmt:message
                    key="mainPage.toRegistration"/></a></li>
        </c:when>
        <c:otherwise>
            <li><a href=${pageContext.request.contextPath}/controller?command=logout><fmt:message
                    key="mainPage.toLogout"/></a></li>
            <li><a href=${pageContext.request.contextPath}/controller?command=rating><fmt:message
                    key="mainPage.toUsersRating"/></a></li>
            <li><a href="${pageContext.request.contextPath}/controller?command=show_bicycles"><fmt:message
                    key="mainPage.toBicycles"/></a></li>
            <c:choose>
                <c:when test="${user.role=='user'}">
                    <li><a href=${pageContext.request.contextPath}/controller?command=profile><fmt:message
                            key="mainPage.toProfile"/></a></li>
                    <li><a href=${pageContext.request.contextPath}/controller?command=new_order><fmt:message
                            key="mainPage.newOrder"/></a></li>
                </c:when>
                <c:otherwise>
                    <li><a href=${pageContext.request.contextPath}/controller?command=users><fmt:message
                            key="mainPage.toUsers"/></a></li>
                    <li><a href=${pageContext.request.contextPath}/controller?command=orders><fmt:message
                            key="mainPage.toOrders"/></a></li>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</ul>
</body>
</html>