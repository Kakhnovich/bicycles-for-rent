<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<c:if test="${requestScope.page==null}">
    <c:set var="page" scope="request" value="1"/>
</c:if>
<h5>
    <c:if test="${requestScope.page>1}">
        <c:if test="${requestScope.page>2}">
            <a href="${pageContext.request.contextPath}/controller?command=${requestScope.cmnd}&page=1&column=${requestScope.page}">1</a>
            <c:if test="${requestScope.page>3}">
                ...
            </c:if>
        </c:if>
        <a href="${pageContext.request.contextPath}/controller?command=${requestScope.cmnd}&page=${requestScope.page-1}&column=${requestScope.column}">${requestScope.page-1}</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/controller?command=${requestScope.cmnd}&page=${requestScope.page}&column=${requestScope.column}">${requestScope.page}</a>
    <c:if test="${requestScope.count>requestScope.page}">
        <a href="${pageContext.request.contextPath}/controller?command=${requestScope.cmnd}&page=${requestScope.page+1}&column=${requestScope.column}">${requestScope.page+1}</a>
        <c:if test="${requestScope.count>requestScope.page+1}">
            <c:if test="${requestScope.count>requestScope.page+2}">
                ...
            </c:if>
            <a href="${pageContext.request.contextPath}/controller?command=${requestScope.cmnd}&page=${requestScope.count}&column=${requestScope.column}">${requestScope.count}</a>
        </c:if>
    </c:if>
</h5>
</body>
</html>
