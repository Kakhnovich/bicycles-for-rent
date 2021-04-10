<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tag" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<c:set var="cmnd" value="orders" scope="request"/>
<c:if test="${requestScope.page==null}">
    <c:set var="page" scope="request" value="1"/>
</c:if>
<html>
<head>
    <link href="styles/style.css" rel="stylesheet" type="text/css">
    <title><fmt:message key="orders.title"/></title>
</head>
<body>
<jsp:include page="/commands.jsp"/>
<div class="mainBody">
    <h2><fmt:message key="orders.msg"/></h2>
    <c:if test="${not empty requestScope.orders}">
        <table border="1">
            <tr>
                <c:choose>
                    <c:when test="${column=='id'}">
                        <th class="selected"><fmt:message key="global.id"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=id>
                            <fmt:message key="global.id"/></a></b></th>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${column=='person_id'}">
                        <th class="selected"><fmt:message key="orders.user"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=person_id>
                            <fmt:message key="orders.user"/></a></b></th>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${column=='bicycle_id'}">
                        <th class="selected"><fmt:message key="orders.bicycle"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=bicycle_id>
                            <fmt:message key="orders.bicycle"/></a></b></th>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${column=='hours'}">
                        <th class="selected"><fmt:message key="orders.hours"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=hours>
                            <fmt:message key="orders.hours"/></a></b></th>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${column=='status'}">
                        <th class="selected"><fmt:message key="orders.status"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=status>
                            <fmt:message key="orders.status"/></a></b></th>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${column=='date'}">
                        <th class="selected"><fmt:message key="orders.date"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=orders&column=date>
                            <fmt:message key="orders.date"/></a></b></th>
                    </c:otherwise>
                </c:choose>
            </tr>
            <c:set var="id" value="1"/>
            <c:forEach var="order" items="${requestScope.orders}">
                <tr>
                    <td>${id}</td>
                    <td>${order.userName}</td>
                    <td>${order.bicycleModel}</td>
                    <td>${order.hours}</td>
                    <td class="button">
                        <c:choose>
                            <c:when test="${order.status=='in processing'}">
                                <form action="${pageContext.request.contextPath}/controller?command=change_order_status&selectedOrder=${id}&option=accept"
                                      method="post">
                                    <input type="submit" value="<fmt:message key="orders.operation.accept"/>">
                                </form>
                                <form action="${pageContext.request.contextPath}/controller?command=change_order_status&selectedOrder=${id}&option=cancel"
                                      method="post">
                                    <input type="submit" value="<fmt:message key="orders.operation.cancel"/>">
                                </form>
                            </c:when>
                            <c:otherwise>
                                ${order.status}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${order.date}</td>
                </tr>
                <c:set var="id" value="${id+1}"/>
            </c:forEach>
        </table>
        <ctg:paginationTag countOfPages="${requestScope.count}" pageNumber="${requestScope.page}"
                           command="${requestScope.cmnd}" column="${requestScope.column}"/>
    </c:if>
</div>
</body>
</html>
