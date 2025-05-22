<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">科目別成績一覧</c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績一覧（科目）</h2>

      <div class="mb-3">
        <p>科目：${subject.name}</p>
      </div>

      <table class="table table-bordered">
        <thead class="table-light">
          <tr>
            <th>入学年度</th>
            <th>クラス</th>
            <th>学生番号</th>
            <th>氏名</th>
            <th>1回</th>
            <th>2回</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="student" items="${students}">
            <tr>
              <td>${entYear}</td>
              <td>${classNum}</td>
              <td>${student.no}</td>
              <td>${student.name}</td>
              <td>
                <c:set var="testResults1" value="${testResults[student.no][1]}" />
                <c:out value="${testResults1 != null ? testResults1 : '-'}" />
              </td>
              <td>
                <c:set var="testResults2" value="${testResults[student.no][2]}" />
                <c:out value="${testResults2 != null ? testResults2 : '-'}" />
              </td>
            </tr>
          </c:forEach>
        </tbody>
      </table>

      <div class="mt-3">
        <a href="TestList.action" class="btn btn-secondary">成績参照に戻る</a>
      </div>
    </section>
  </c:param>
</c:import>