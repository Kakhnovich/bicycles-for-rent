<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="custom_tag" %>
<c:if test="${sessionScope.locale==null}">
    <c:set var="locale" value="en" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="messages"/>
<c:set var="cmnd" value="show_bicycles" scope="request"/>
<c:if test="${requestScope.page==null}">
    <c:set var="page" scope="request" value="1"/>
</c:if>
<html>
<head>
    <link href="styles/style.css" rel="stylesheet" type="text/css">
    <title><fmt:message key="bicycles.title"/></title>
</head>
<body>
<jsp:include page="/commands.jsp"/>
<div class="mainBody">
    <h2><fmt:message key="bicycles.list"/></h2>
    <c:if test="${not empty requestScope.bicycles}">
        <table border="1">
            <tr>
                <c:choose>
                    <c:when test="${column=='model'}">
                        <th class="selected"><fmt:message key="bicycles.model"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=show_bicycles&column=model>
                            <fmt:message key="bicycles.model"/></a></b></th>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${column=='price'}">
                        <th class="selected"><fmt:message key="bicycles.price"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=show_bicycles&column=price>
                            <fmt:message key="bicycles.price"/></a></b></th>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${column=='address'}">
                        <th class="selected"><fmt:message key="bicycles.place"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a
                                href=${pageContext.request.contextPath}/controller?command=show_bicycles&column=address>
                            <fmt:message key="bicycles.place"/></a></b></th>
                    </c:otherwise>
                </c:choose>

                <c:choose>
                    <c:when test="${column=='count'}">
                        <th class="selected"><fmt:message key="bicycles.count"/></th>
                    </c:when>
                    <c:otherwise>
                        <th><b><a href=${pageContext.request.contextPath}/controller?command=show_bicycles&column=count>
                            <fmt:message key="bicycles.count"/></a></b></th>
                    </c:otherwise>
                </c:choose>
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
        <ctg:paginationTag countOfPages="${requestScope.count}" pageNumber="${requestScope.page}"
                           command="${requestScope.cmnd}" column="${requestScope.column}"/>
    </c:if>
    <br>
    <br>
    <form class="forms" action="${pageContext.request.contextPath}/controller?command=change_bicycles_count"
          method="post">
        <fmt:message key="bicycles.operation"/>
        <div><select class="text" name="selectedOption">
            <option value="<fmt:message key="bicycles.operation.add"/>"><fmt:message
                    key="bicycles.operation.add"/></option>
            <option value="<fmt:message key="bicycles.operation.remove"/>"><fmt:message
                    key="bicycles.operation.remove"/></option>
        </select></div>
        <div><input class="text" type="text" placeholder="<fmt:message key="bicycles.model"/>" name="model"></div>
        <div><input class="text" type="text" placeholder="<fmt:message key="bicycles.place"/>" name="place"></div>
        <div><input class="text" type="number" placeholder="<fmt:message key="bicycles.count"/>" name="count" min="1">
        </div>
        <div><input class="formButton" type="submit" value="<fmt:message key="global.submit"/>"></div>
    </form>
    <c:if test="${!cookie['errorMessage'].value.equals('')}">
        <p class="error">${cookie['errorMessage'].value}</p>
    </c:if>
</div>
</body>
</html>
