<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">クラス別成績一覧</c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績一覧（科目）</h2>

      <table class="table table-bordered">
        <thead class="table-light">
          <tr>
            <th>科目名</th>
            <th>平均点</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="g" items="${grades}">
            <tr>
              <td>${g.subjectName}</td>
              <td>${g.avgPoint}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <div class="mt-3">
        <a href="TestList.action" class="btn btn-secondary">成績一覧に戻る</a>
      </div>
    </section>
  </c:param>
</c:import>