<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<c:set var="cmnd" value="orders" scope="request"/>
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
            <th><b><a href=${pageContext.request.contextPath}/controller?command=orders><fmt:message
                    key="global.id"/></a></b></th>
            <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=person_id><fmt:message
                    key="orders.userId"/></a></b></th>
            <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=bicycle_id><fmt:message
                    key="orders.bicycleId"/></a></b></th>
            <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=hours><fmt:message
                    key="orders.hours"/></a></b></th>
            <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=status><fmt:message
                    key="orders.status"/></a></b></th>
            <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=date><fmt:message
                    key="orders.date"/></a></b></th>
        </tr>
        <c:forEach var="order" items="${requestScope.orders}">
            <tr>
                <td>${order.id}</td>
                <td>${order.user_id}</td>
                <td>${order.bicycle_id}</td>
                <td>${order.hours}</td>
                <td>
                    <c:choose>
                        <c:when test="${order.status=='in processing'}">
                            <form action="${pageContext.request.contextPath}/controller?command=change_order_status&selectedOrder=${order.id}&option=accept"
                                  method="post">
                                <input type="submit" value="accept">
                            </form>
                            <form action="${pageContext.request.contextPath}/controller?command=change_order_status&selectedOrder=${order.id}&option=cancel"
                                  method="post">
                                <input type="submit" value="cancel">
                            </form>
                        </c:when>
                        <c:otherwise>
                            ${order.status}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${order.date}</td>
            </tr>
        </c:forEach>
    </table>
    <jsp:include page="/pagination.jsp"/>
</c:if>
<br>
<br>
<br>
<ul>
    <li><a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a></li>
</ul>
<jsp:include page="/commands.jsp"/>
</body>
</html>
