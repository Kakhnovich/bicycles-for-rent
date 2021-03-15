<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<c:if test="${user.role!='admin'}">
    <jsp:forward page="main.jsp"/>
</c:if>
<html>
<head>
    <title><fmt:message key="orders.title"/></title>
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
<c:if test="${not empty requestScope.orders}">
    <table border="1" width="800">
        <tr bgcolor="00FF7F">
            <th><b><fmt:message key="global.id"/></b></th>
            <th><b><fmt:message key="orders.userId"/></b></th>
            <th><b><fmt:message key="orders.bicycleId"/></b></th>
            <th><b><fmt:message key="orders.hours"/></b></th>
            <th><b><fmt:message key="orders.status"/></b></th>
            <th><b><fmt:message key="orders.date"/></b></th>
        </tr>
        <c:forEach var="order" items="${requestScope.orders}">
            <tr>
                <td>${order.id}</td>
                <td>${order.user_id}</td>
                <td>${order.bicycle_id}</td>
                <td>${order.hours}</td>
                <td>${order.status}</td>
                <td>${order.date}</td>
            </tr>
        </c:forEach>
    </table>
    <br>
    <h4><a href=${pageContext.request.contextPath}/controller?command=orders><fmt:message
            key="orders.operation.all"/></a></h4>
    <h4><a href=${pageContext.request.contextPath}/controller?command=orders&byValue=user><fmt:message
            key="orders.operation.byUsers"/></a></h4>
    <h4><a href=${pageContext.request.contextPath}/controller?command=orders&byValue=date><fmt:message
            key="orders.operation.byDate"/></a></h4>
    <h4><a href=${pageContext.request.contextPath}/controller?command=orders&byValue=status><fmt:message
            key="orders.operation.byStatus"/></a></h4>
    <br>
    <br>
    <h3><fmt:message key="orders.operation"/></h3>
    <form action="${pageContext.request.contextPath}/controller?command=change_order_status" method="post">
        <fmt:message key="orders.order"/>: <select name="selectedOrder">
        <jsp:useBean id="ordersInProcess" scope="request" type="java.util.List"/>
        <option selected="selected">Please Select</option>
        <c:forEach items="${ordersInProcess}" var="orderInProcess">
            <option value="${orderInProcess.id}">${orderInProcess.id}</option>
        </c:forEach>
    </select>
        <fmt:message key="orders.operation.accept"/>: <input type="checkbox" name="chkBox">
        <input type="submit" value="<fmt:message key="global.submit"/>">
    </form>
</c:if>
<br>
<br>
<br>
<a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a>
</body>
</html>
