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
    <link href="styles/style.css" rel="stylesheet">
</head>
<body>
<header>
    <div class="sect">
        <div class="logo"><a href="${pageContext.request.contextPath}/controller?command=main">
            <img src="img/logo.png" alt="Bicycle rent" height="80"/></a>
        </div>
        <div class="firstSect">
            <c:choose>
                <c:when test="${user.login==null}">
                    <form action="${pageContext.request.contextPath}/controller?command=login" method="post">
                        <input class="logging" type="submit" value="<fmt:message key="header.login"/>"/>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="${pageContext.request.contextPath}/controller?command=logout" method="post">
                        <input class="logging" type="submit" value="<fmt:message key="header.logout"/>">
                    </form>
                </c:otherwise>
            </c:choose>
            <div class="locale">
                <div><b><fmt:message key="global.locale"/></b></div>
                <div class="lang">
                    <div><a href="${pageContext.request.contextPath}/controller?command=change_locale&locale=en">
                        <fmt:message key="global.en"/></a></div>
                    <div><a href="${pageContext.request.contextPath}/controller?command=change_locale&locale=ru">
                        <fmt:message key="global.ru"/></a></div>
                    <div><a href="${pageContext.request.contextPath}/controller?command=change_locale&locale=cn">
                        <fmt:message key="global.cn"/></a></div>
                </div>
            </div>
        </div>
    </div>
</header>
<nav>
    <c:if test="${user.login!=null}">

        <c:choose>
            <c:when test="${command=='rating'}">
                <div class="selected"><b><fmt:message key="nav.rating"/></b></div>
            </c:when>
            <c:otherwise>
                <div><b><a href=${pageContext.request.contextPath}/controller?command=rating><fmt:message
                        key="nav.rating"/></a></b></div>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${command=='profile'}">
                <div class="selected"><b><fmt:message key="nav.profile"/></b></div>
            </c:when>
            <c:otherwise>
                <div><b><a href=${pageContext.request.contextPath}/controller?command=profile><fmt:message
                        key="nav.profile"/></a></b></div>
            </c:otherwise>
        </c:choose>

        <c:if test="${user.role!='user'}">

            <c:choose>
                <c:when test="${command=='users'}">
                    <div class="selected"><b><fmt:message key="nav.users"/></b></div>
                </c:when>
                <c:otherwise>
                    <div><b><a href=${pageContext.request.contextPath}/controller?command=users><fmt:message
                            key="nav.users"/></a></b></div>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${command=='show_bicycles'}">
                    <div class="selected"><b><fmt:message key="nav.bicycles"/></b></div>
                </c:when>
                <c:otherwise>
                    <div><b><a href=${pageContext.request.contextPath}/controller?command=show_bicycles><fmt:message
                            key="nav.bicycles"/></a></b>
                    </div>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${command=='orders'}">
                    <div class="selected"><b><fmt:message key="nav.orders"/></b></div>
                </c:when>
                <c:otherwise>
                    <div><b><a href=${pageContext.request.contextPath}/controller?command=orders><fmt:message
                            key="nav.orders"/> </a></b></div>
                </c:otherwise>
            </c:choose>

        </c:if>
    </c:if>
</nav>
</body>
</html>
