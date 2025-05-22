<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>

  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績登録</h2>

      <c:if test="${not empty error}">
        <div class="alert alert-danger mx-3">
          ${error}
        </div>
      </c:if>

      <form action="TestRegist.action" method="get">
        <div class="row border mx-3 py-2 mb-3 rounded align-items-center">
          <div class="col-md-3">
            <label class="form-label">入学年度</label>
            <select class="form-select" name="ent_year">
              <option value="">-------</option>
              <c:forEach var="year" items="${entYearList}">
                <option value="${year}" <c:if test="${selectedEntYear == year}">selected</c:if>>${year}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-md-3">
            <label class="form-label">クラス</label>
            <select class="form-select" name="class_num">
              <option value="">-------</option>
              <c:forEach var="cls" items="${classNumList}">
                <option value="${cls}" <c:if test="${selectedClassNum == cls}">selected</c:if>>${cls}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-md-3">
            <label class="form-label">科目</label>
            <select class="form-select" name="subject_cd">
              <option value="">-------</option>
              <c:forEach var="subject" items="${subjectList}">
                <option value="${subject.cd}" <c:if test="${selectedSubjectCd == subject.cd}">selected</c:if>>${subject.name}</option>
              </c:forEach>
            </select>
          </div>
          <div class="col-md-3">
            <label class="form-label">回数</label>
            <select class="form-select" name="no">
              <option value="">-------</option>
              <c:forEach var="num" items="${noList}">
                <option value="${num}" <c:if test="${selectedNo == num}">selected</c:if>>${num}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="text-center mb-4">
          <button type="submit" class="btn btn-secondary">検索</button>
        </div>
      </form>

      <div class="mt-3 mx-3">
      </div>
    </section>
  </c:param>
</c:import>