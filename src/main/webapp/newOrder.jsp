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
    <title>New Order</title>
</head>
<body>
<jsp:include page="/commands.jsp"/>
<div class="mainBody">
    <h2><fmt:message key="newOrder.msg"/>:</h2>
    <c:set var="number" value="1" scope="request"/>
    <c:forEach var="bicycle" items="${requestScope.bicycles}">
            <form class="bicycles"
                  action="${pageContext.request.contextPath}/controller?command=new_order&model=${bicycle.value.model}"
                  method="post">
                <div class="forms">
                    <div><b>${bicycle.value.model}</b></div>
                    <div><fmt:message key="newOrder.price"/> - <i>${bicycle.value.price}</i></div>
                    <div><input class="text" name="hours" type="number" required min="1" max="24"
                                placeholder="<fmt:message key="newOrder.hours"/>"></div>
                    <input class="formButton" type="submit" value="<fmt:message key="newOrder.button"/>">
                </div>
                <div><img class="img" src="img/bicycle${bicycle.key}.jpg" alt="bicycle${bicycle.key}"></div>
            </form>
            <br>
        <c:set var="number" value="${number+1}"/>
        </c:forEach>
    </div>
</body>
</html>
