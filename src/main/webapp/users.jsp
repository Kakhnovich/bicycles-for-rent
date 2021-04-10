<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tag" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<c:set var="cmnd" value="users" scope="request"/>
<c:if test="${requestScope.page==null}">
    <c:set var="page" scope="request" value="1"/>
</c:if>
<html>
<head>
    <link href="styles/style.css" rel="stylesheet" type="text/css">
    <title><fmt:message key="users.title"/></title>
</head>
<body>
<jsp:include page="/commands.jsp"/>
<div class="mainBody">
    <h2><fmt:message key="users.msg"/></h2>
    <table border="1">
        <tr>
            <c:choose>
                <c:when test="${column=='id'}">
                    <th class="selected"><fmt:message key="global.id"/></th>
                </c:when>
                <c:otherwise>
                    <th><b><a href=${pageContext.request.contextPath}/controller?command=users&column=id><fmt:message
                            key="global.id"/></a></b></th>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${column=='status_id'}">
                    <th class="selected"><fmt:message key="profile.role"/></th>
                </c:when>
                <c:otherwise>
                    <th><b><a
                            href=${pageContext.request.contextPath}/controller?command=users&column=status_id><fmt:message
                            key="profile.role"/></a></b></th>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${column=='login'}">
                    <th class="selected"><fmt:message key="login.title"/></th>
                </c:when>
                <c:otherwise>
                    <th><b><a href=${pageContext.request.contextPath}/controller?command=users&column=login><fmt:message
                            key="login.title"/></a></b></th>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${column=='balance'}">
                    <th class="selected"><fmt:message key="profile.balance"/></th>
                </c:when>
                <c:otherwise>
                    <th><b><a
                            href=${pageContext.request.contextPath}/controller?command=users&column=balance><fmt:message
                            key="profile.balance"/></a></b></th>
                </c:otherwise>
            </c:choose>

            <c:choose>
                <c:when test="${column=='banned'}">
                    <th class="selected"><fmt:message key="users.banned"/></th>
                </c:when>
                <c:otherwise>
                    <th><b><a
                            href=${pageContext.request.contextPath}/controller?command=users&column=banned><fmt:message
                            key="users.banned"/></a></b></th>
                </c:otherwise>
            </c:choose>

            <th><b><fmt:message key="users.operation.promote"/></b></th>
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
                <td class="button"><c:choose>
                    <c:when test="${!user.banned}">
                        <form action="${pageContext.request.contextPath}/controller?command=change_user_information&selectedUser=${user.login}&option=ban"
                              method="post">
                            <input type="submit" value="<fmt:message key="users.operation.ban"/>">
                        </form>
                    </c:when>
                    <c:otherwise>
                        <fmt:message key="users.banned"/>
                    </c:otherwise>
                </c:choose>
                </td>
                <td class="button">
                    <c:if test="${!user.banned && user.roleId==2}">
                        <form action="${pageContext.request.contextPath}/controller?command=change_user_information&selectedUser=${user.login}&option=promote"
                              method="post">
                            <input type="submit" value="<fmt:message key="users.operation.promote"/>">
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
    <ctg:paginationTag countOfPages="${requestScope.count}" pageNumber="${requestScope.page}"
                       command="${requestScope.cmnd}" column="${requestScope.column}"/>
</div>
</body>
</html>
