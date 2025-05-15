<%-- subject_list.jsp の完全修正版 - インラインスタイル --%>
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
				<a href="SubjectCreate.action">新規登録</a>
			</div>

			<!-- 科目一覧表示 -->
			<table style="width: 100%; border-collapse: collapse; margin-bottom: 20px; border: 1px solid #e0e0e0;">
				<tr>
					<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">科目コード</th>
					<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">科目名</th>
					<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;"></th>
					<th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;"></th>
				</tr>
				<c:choose>
					<c:when test="${not empty subjects}">
						<c:forEach var="subject" items="${subjects}">
							<tr>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${subject.cd}</td>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${subject.name}</td>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">
									<a href="SubjectUpdate.action?cd=${subject.cd}" style="color: #0000ff; text-decoration: none;">変更</a>
								</td>
								<td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">
									<a href="SubjectDelete.action?cd=${subject.cd}" style="color: #0000ff; text-decoration: none;">削除</a>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="4" style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: center; background-color: #ffffff;">科目情報がありません</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>
		</section>
	</c:param>
</c:import>