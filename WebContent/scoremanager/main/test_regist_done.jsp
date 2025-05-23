<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

      <!-- 登録完了メッセージ -->
      <div class="alert alert-success mx-3 text-center">

        <p class="mb-0">登録が完了しました</p>
      </div>

      <!-- 操作リンク -->
      <div class="mx-3 mt-4">
        <a href="TestRegist.action" class="btn btn-primary me-3">戻る</a>
        <a href="TestList.action" class="btn btn-outline-primary">成績参照</a>
      </div>

      </div>
    </section>
  </c:param>
</c:import>