<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<li>
    <span class="folder-label"><c:out value="${folder.label}" escapeXml="false"/></span>
    <c:if test="${not empty folder.children}">
        <ul class="nested">
            <c:forEach var="child" items="${folder.children}">
                <jsp:include page="folderNode.jsp">
                    <jsp:param name="folder" value="${child}" />
                </jsp:include>
            </c:forEach>
        </ul>
    </c:if>
</li>