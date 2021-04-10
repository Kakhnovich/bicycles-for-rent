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
    <title><fmt:message key="login.title"/></title>
</head>
<body>
<jsp:include page="/commands.jsp"/>
<div class="mainBody">
    <h2><fmt:message key="login.msg"/></h2>
    <form class="forms" action="${pageContext.request.contextPath}/controller?command=login" method="post">
        <div><input class="text" name="userName" placeholder="<fmt:message key="login.title"/>"></div>
        <div><input class="text" type="password" name="userPassword"
                    placeholder="<fmt:message key="login.userPassword"/>"></div>
        <div><input class="formButton" type="submit" value="<fmt:message key="global.submit"/>"></div>
        <c:if test="${!cookie['errorMessage'].value.equals('')}">
            <p class="error">${cookie['errorMessage'].value}</p>
        </c:if>
    </form>
    <p><fmt:message key="login.regMsg"/>? <a
            href=${pageContext.request.contextPath}/controller?command=sign_up><fmt:message
            key="login.toRegistration"/></a></p>
</div>
</body>
</html>
