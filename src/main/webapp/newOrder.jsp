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
    <title>New Order</title>
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
<h4>Available bicycles for this day:</h4>
<c:forEach var="bicycle" items="${requestScope.bicycles}">
    <form action="${pageContext.request.contextPath}/controller?command=new_order&model=${bicycle.model}" method="post">
        <h5>${bicycle.model}</h5>
        <h5>Price per hour - ${bicycle.price}</h5>
        <h5><input name="hours" type="number" min="1" placeholder="Count of hours"></h5>
        <input type="submit" value="to order">
    </form>
    <br>
</c:forEach>
<br>
<br>
<br>
<ul>
    <li><a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a></li>
</ul>
<jsp:include page="/commands.jsp"/>
</body>
</html>
