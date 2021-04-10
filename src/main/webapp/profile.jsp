<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tag" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<c:set var="cmnd" value="profile" scope="request"/>
<c:if test="${requestScope.page==null}">
    <c:set var="page" scope="request" value="1"/>
</c:if>
<html>
<head>
    <link href="styles/style.css" rel="stylesheet" type="text/css">
    <title><fmt:message key="profile.title"/></title>
</head>
<body>
<jsp:include page="/commands.jsp"/>
<div class="mainBody">
    <h2><fmt:message key="profile.msg"/></h2>
    <div class="info"><b><fmt:message key="login.title"/></b> - ${user.login}</div>
    <div class="info"><b><fmt:message key="profile.balance"/></b> - ${user.balance} <fmt:message key="profile.money"/></div>
    <form class="balance" action="${pageContext.request.contextPath}/controller?command=change_balance" method="post">
        <fmt:message key="profile.operation.changeBalance"/>
        <div><input class="text" type="number" placeholder="<fmt:message key="profile.balance"/>" name="money" min="1">
        <input class="formButton" type="submit" value="<fmt:message key="profile.operation.replenish"/>"></div>
        <p>(<fmt:message key="profile.operation.changeBalance.msg"/>)</p>
    </form>
    <c:if test="${not empty requestScope.orders}">
        <div><b><fmt:message key="orders.title"/></b>:</div>
        <table border="1">
            <tr>
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
        <ctg:paginationTag countOfPages="${requestScope.count}" pageNumber="${requestScope.page}"
                           command="${requestScope.cmnd}" column="id"/>
    </c:if>
</div>
</body>
</html>
