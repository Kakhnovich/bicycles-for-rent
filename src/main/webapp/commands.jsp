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
    <title>Commands</title>
</head>
<body>
<ul>
    <c:choose>
        <c:when test="${user.login==null}">
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
            <li><a href=${pageContext.request.contextPath}/controller?command=profile><fmt:message
                    key="mainPage.toProfile"/></a></li>
            <c:if test="${user.role!='user'}">
                <li><a href=${pageContext.request.contextPath}/controller?command=users><fmt:message
                        key="mainPage.toUsers"/></a></li>
                <li><a href="${pageContext.request.contextPath}/controller?command=show_bicycles"><fmt:message
                        key="mainPage.toBicycles"/></a></li>
                <li><a href=${pageContext.request.contextPath}/controller?command=orders><fmt:message
                        key="mainPage.toOrders"/></a></li>
            </c:if>
        </c:otherwise>
    </c:choose>
</ul>
</body>
</html>
