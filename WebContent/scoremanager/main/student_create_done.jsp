```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">学生登録完了</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-success bg-opacity-25 py-2 px-4">学生登録完了</h2>

      <div class="alert alert-success">
        <p>学生情報の登録が完了しました。</p>
      </div>

      <div class="mt-3">
        <a href="StudentCreate.action" class="btn btn-primary">続けて登録</a>
        <a href="StudentList.action" class="btn btn-link">学生一覧に戻る</a>
      </div>
    </section>
  </c:param>
</c:import>
```