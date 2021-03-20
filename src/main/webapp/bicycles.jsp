<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<c:set var="cmnd" value="show_bicycles" scope="request"/>
<html>
<head>
    <title><fmt:message key="bicycles.title"/></title>
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
<h2><fmt:message key="bicycles.list"/></h2>
<c:if test="${not empty requestScope.bicycles}">
    <table border="1" width="600">
        <tr bgcolor="00FF7F">
            <th><b><a href=${pageContext.request.contextPath}/controller?command=show_bicycles&column=model><fmt:message key="bicycles.model"/></a></b></th>
            <th><b><a href=${pageContext.request.contextPath}/controller?command=show_bicycles&column=price><fmt:message key="bicycles.price"/></a></b></th>
            <th><b><a href=${pageContext.request.contextPath}/controller?command=show_bicycles&column=address><fmt:message key="bicycles.place"/></a></b></th>
            <th><b><a href=${pageContext.request.contextPath}/controller?command=show_bicycles&column=count><fmt:message key="bicycles.count"/></a></b></th>
        </tr>
        <c:forEach var="bicycle" items="${requestScope.bicycles}">
            <tr>
                <td>${bicycle.model}</td>
                <td>${bicycle.price}</td>
                <td>${bicycle.place}</td>
                <td>${bicycle.count}</td>
            </tr>
        </c:forEach>
    </table>
    <jsp:include page="/pagination.jsp"/>
</c:if>
<br>
<br>
<h3><fmt:message key="bicycles.operation"/></h3>
<form action="${pageContext.request.contextPath}/controller?command=change_bicycles_count" method="post">
    <select name="selectedOption">
        <option value="add"><fmt:message key="bicycles.operation.add"/></option>
        <option value="remove"><fmt:message key="bicycles.operation.remove"/></option>
    </select>
    <input type="text" placeholder="<fmt:message key="bicycles.model"/>" name="model">
    <input type="text" placeholder="<fmt:message key="bicycles.place"/>" name="place">
    <input type="number" placeholder="<fmt:message key="bicycles.price"/>" name="price" step="0.01" min="0">
    <input type="number" placeholder="<fmt:message key="bicycles.count"/>" name="count" min="1">
    <input type="submit" value="<fmt:message key="global.submit"/>">
</form>
<br>
<br>
<br>
<ul>
    <li><a href=${pageContext.request.contextPath}/controller><fmt:message key="global.toMain"/></a></li>
</ul>
<jsp:include page="/commands.jsp"/>
</body>
</html>
