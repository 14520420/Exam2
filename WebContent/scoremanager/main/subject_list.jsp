<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
	<c:param name="title">得点管理システム</c:param>
	<c:param name="scripts"></c:param>
	<c:param name="content">
		<section class="me-4">
			<h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目管理</h2>

			<!-- 新規登録リンク -->
			<div class="my-2 text-end px-4">
				<a href="SubjectCreate.action" class="btn btn-primary btn-sm">新規登録</a>
			</div>

			<!-- 科目一覧表示 -->
			<c:choose>
				<c:when test="${not empty subjects && subjects.size() > 0}">
					<table class="table table-hover">
						<thead>
							<tr>
								<th>科目コード</th>
								<th>科目名</th>
								<th></th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="subject" items="${subjects}">
								<tr>
									<td>${subject.cd}</td>
									<td>${subject.name}</td>
									<td><a href="SubjectUpdate.action?cd=${subject.cd}">編集</a></td>
									<td><a href="SubjectDelete.action?cd=${subject.cd}" class="text-danger" onclick="return confirm('本当に削除しますか？');">削除</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<div class="alert alert-info">
						科目情報がありません。
					</div>
				</c:otherwise>
			</c:choose>

			<div class="mt-3">
			</div>
		</section>
	</c:param>
</c:import>